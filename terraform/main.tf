terraform {
  required_version = ">= 1.5"
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
    random = {
      source  = "hashicorp/random"
      version = "~> 3.0"
    }
  }
}

provider "aws" {
  region  = var.aws_region
  profile = var.aws_profile
}

# --- S3 bucket for Elastic Beanstalk application versions ---
resource "aws_s3_bucket" "eb_bucket" {
  bucket        = "${var.app_name}-deployments-${random_id.suffix.hex}"
  force_destroy = true
}

resource "random_id" "suffix" {
  byte_length = 4
}

# --- IAM role for Elastic Beanstalk EC2 instances ---
resource "aws_iam_instance_profile" "eb_profile" {
  name = "${var.app_name}-eb-profile"
  role = aws_iam_role.eb_ec2_role.name
}

resource "aws_iam_role" "eb_ec2_role" {
  name = "${var.app_name}-eb-ec2-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Action    = "sts:AssumeRole"
      Effect    = "Allow"
      Principal = { Service = "ec2.amazonaws.com" }
    }]
  })
}

resource "aws_iam_role_policy_attachment" "eb_web_tier" {
  role       = aws_iam_role.eb_ec2_role.name
  policy_arn = "arn:aws:iam::aws:policy/AWSElasticBeanstalkWebTier"
}

resource "aws_iam_role_policy_attachment" "eb_docker" {
  role       = aws_iam_role.eb_ec2_role.name
  policy_arn = "arn:aws:iam::aws:policy/AWSElasticBeanstalkMulticontainerDocker"
}

# --- Elastic Beanstalk Application ---
resource "aws_elastic_beanstalk_application" "app" {
  name        = var.app_name
  description = "Franchise API - Prueba Tecnica NEQUI"
}

# --- Empaquetado de la Aplicación (ZIP) ---
data "archive_file" "deploy_zip" {
  type        = "zip"
  output_path = "${path.module}/../deploy.zip"

  source {
    content  = file("${path.module}/../Dockerfile.eb")
    filename = "Dockerfile"
  }

  source {
    content  = file("${path.module}/../target/franchise-api-0.0.1-SNAPSHOT.jar")
    filename = "franchise-api-0.0.1-SNAPSHOT.jar"
  }
}

resource "aws_s3_object" "app_version" {
  bucket = aws_s3_bucket.eb_bucket.id
  key    = "deploy-${data.archive_file.deploy_zip.output_md5}.zip"
  source = data.archive_file.deploy_zip.output_path
}

resource "aws_elastic_beanstalk_application_version" "app_version" {
  name        = "${var.app_name}-${data.archive_file.deploy_zip.output_md5}"
  application = aws_elastic_beanstalk_application.app.name
  bucket      = aws_s3_bucket.eb_bucket.id
  key         = aws_s3_object.app_version.id
}

# --- Elastic Beanstalk Environment ---
resource "aws_elastic_beanstalk_environment" "env" {
  name                = "${var.app_name}-${var.environment}"
  application         = aws_elastic_beanstalk_application.app.name
  solution_stack_name = var.solution_stack
  version_label       = aws_elastic_beanstalk_application_version.app_version.name

  setting {
    namespace = "aws:autoscaling:launchconfiguration"
    name      = "IamInstanceProfile"
    value     = aws_iam_instance_profile.eb_profile.name
  }

  setting {
    namespace = "aws:autoscaling:launchconfiguration"
    name      = "InstanceType"
    value     = "t3.small"
  }

  setting {
    namespace = "aws:elasticbeanstalk:application:environment"
    name      = "MONGODB_URI"
    value     = var.mongodb_uri
  }

  setting {
    namespace = "aws:elasticbeanstalk:application:environment"
    name      = "SERVER_PORT"
    value     = "8080"
  }

  setting {
    namespace = "aws:elasticbeanstalk:application:environment"
    name      = "JWT_SECRET"
    value     = var.jwt_secret
  }

  setting {
    namespace = "aws:elasticbeanstalk:application:environment"
    name      = "ADMIN_USERNAME"
    value     = var.admin_username
  }

  setting {
    namespace = "aws:elasticbeanstalk:application:environment"
    name      = "ADMIN_PASSWORD"
    value     = var.admin_password
  }

  setting {
    namespace = "aws:elasticbeanstalk:environment"
    name      = "EnvironmentType"
    value     = "SingleInstance"
  }
}
