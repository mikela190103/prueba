output "cluster_name" {
  description = "ECS Cluster name"
  value       = aws_ecs_cluster.main.name
}

output "cluster_id" {
  description = "ECS Cluster ID"
  value       = aws_ecs_cluster.main.id
}

output "service_name" {
  description = "ECS Service name"
  value       = aws_ecs_service.main.name
}

output "service_id" {
  description = "ECS Service ID"
  value       = aws_ecs_service.main.id
}

output "load_balancer_dns" {
  description = "Load Balancer DNS name"
  value       = aws_lb.main.dns_name
}

output "load_balancer_arn" {
  description = "Load Balancer ARN"
  value       = aws_lb.main.arn
}

output "target_group_arn" {
  description = "Target Group ARN"
  value       = aws_lb_target_group.main.arn
}

