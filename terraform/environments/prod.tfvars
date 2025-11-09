environment         = "prod"
aws_region         = "us-east-1"
project_name       = "devops-payload"
vpc_cidr           = "10.0.0.0/16"
availability_zones = ["us-east-1a", "us-east-1b"]

container_port = 8080
desired_count  = 2
min_capacity   = 2
max_capacity   = 10

cpu_target_value    = 70
memory_target_value = 80

