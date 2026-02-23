variable "aws_region" {
  description = "AWS region"
  type        = string
  default     = "us-east-1"
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
  default     = "64bit Amazon Linux 2023 v4.3.5 running Docker"
}
