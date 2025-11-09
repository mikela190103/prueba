environment         = "dev"
aws_region         = "us-east-1"
project_name       = "devops-payload"
vpc_cidr           = "10.1.0.0/16"
availability_zones = ["us-east-1a", "us-east-1b"]

container_port = 8080
desired_count  = 1
min_capacity   = 1
max_capacity   = 4

cpu_target_value    = 75
memory_target_value = 85

