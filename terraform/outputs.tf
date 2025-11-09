output "vpc_id" {
  description = "VPC ID"
  value       = module.networking.vpc_id
}

output "ecr_repository_url" {
  description = "ECR repository URL"
  value       = module.ecr.repository_url
}

output "load_balancer_dns" {
  description = "Load Balancer DNS name"
  value       = module.ecs.load_balancer_dns
}

output "load_balancer_url" {
  description = "Load Balancer URL"
  value       = "http://${module.ecs.load_balancer_dns}"
}

output "ecs_cluster_name" {
  description = "ECS Cluster name"
  value       = module.ecs.cluster_name
}

output "ecs_service_name" {
  description = "ECS Service name"
  value       = module.ecs.service_name
}

