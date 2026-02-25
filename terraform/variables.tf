variable "aws_region" {
  description = "AWS region"
  type        = string
  default     = "us-east-1"
}

variable "aws_profile" {
  description = "AWS CLI profile name"
  type        = string
  default     = "mily-aws-student"
}

variable "app_name" {
  description = "Application name"
  type        = string
  default     = "franchise-api"
}

variable "environment" {
  description = "Environment name"
  type        = string
  default     = "production"
}

variable "mongodb_uri" {
  description = "MongoDB Atlas connection URI"
  type        = string
  sensitive   = true
}

variable "solution_stack" {
  description = "Elastic Beanstalk solution stack for Docker"
  type        = string
  default     = "64bit Amazon Linux 2023 v4.9.3 running Docker"
}

variable "jwt_secret" {
  description = "JWT Secret for application security"
  type        = string
  sensitive   = true
}

variable "admin_username" {
  description = "Admin username for the API"
  type        = string
  sensitive   = true
}

variable "admin_password" {
  description = "Admin password for the API"
  type        = string
  sensitive   = true
}
