terraform {
  required_version = ">= 1.0"
  
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }

  # Usa backend local por defecto
  # Para producción, descomentar y configurar S3:
  # backend "s3" {
  #   bucket         = "your-terraform-state-bucket"
  #   key            = "payload/terraform.tfstate"
  #   region         = "us-east-1"
  #   encrypt        = true
  #   dynamodb_table = "terraform-state-lock"
  # }
}

provider "aws" {
  region = var.aws_region

  default_tags {
    tags = {
      Project     = "DevOps-Payload"
      Environment = var.environment
      ManagedBy   = "Terraform"
    }
  }
}

# VPC and Networking
module "networking" {
  source = "./modules/networking"

  environment         = var.environment
  vpc_cidr           = var.vpc_cidr
  availability_zones = var.availability_zones
  project_name       = var.project_name
}

# ECR Repository
module "ecr" {
  source = "./modules/ecr"

  environment  = var.environment
  project_name = var.project_name
}

# ECS Cluster and Service
module "ecs" {
  source = "./modules/ecs"

  environment        = var.environment
  project_name       = var.project_name
  vpc_id            = module.networking.vpc_id
  private_subnet_ids = module.networking.private_subnet_ids
  public_subnet_ids  = module.networking.public_subnet_ids
  ecr_repository_url = module.ecr.repository_url
  container_image    = var.container_image
  container_port     = var.container_port
  desired_count      = var.desired_count
  min_capacity       = var.min_capacity
  max_capacity       = var.max_capacity
}

