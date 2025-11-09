variable "environment" {
  description = "Environment name"
  type        = string
}

variable "project_name" {
  description = "Project name"
  type        = string
}

variable "vpc_id" {
  description = "VPC ID"
  type        = string
}

variable "private_subnet_ids" {
  description = "List of private subnet IDs"
  type        = list(string)
}

variable "public_subnet_ids" {
  description = "List of public subnet IDs"
  type        = list(string)
}

variable "ecr_repository_url" {
  description = "ECR repository URL"
  type        = string
}

variable "container_image" {
  description = "Container image tag"
  type        = string
  default     = "latest"
}

variable "container_port" {
  description = "Port exposed by the container"
  type        = number
  default     = 8080
}

variable "desired_count" {
  description = "Desired number of tasks"
  type        = number
  default     = 2
}

variable "min_capacity" {
  description = "Minimum capacity for auto-scaling"
  type        = number
  default     = 2
}

variable "max_capacity" {
  description = "Maximum capacity for auto-scaling"
  type        = number
  default     = 10
}

