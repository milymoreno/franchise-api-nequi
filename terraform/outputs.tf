output "app_url" {
  description = "Public URL of the Elastic Beanstalk environment"
  value       = "http://${aws_elastic_beanstalk_environment.env.cname}"
}

output "environment_name" {
  description = "Elastic Beanstalk environment name"
  value       = aws_elastic_beanstalk_environment.env.name
}

output "s3_bucket" {
  description = "S3 bucket used for deployments"
  value       = aws_s3_bucket.eb_bucket.bucket
}
