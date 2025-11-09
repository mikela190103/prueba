# Guía de Despliegue - DevOps Payload Microservice

## Resumen del Proceso

### 1. **Configuración Inicial (Local - Una sola vez)**
   - Instalar herramientas necesarias
   - Configurar credenciales de AWS localmente
   - Desplegar infraestructura base con Terraform
   - Configurar GitHub Secrets

### 2. **Pipeline Automático (GitHub Actions)**
   - Cada push gatilla el pipeline automáticamente
   - Build, Test, Static Analysis
   - Docker build & push a ECR
   - Despliegue automático a ECS

---

## Paso 1: Herramientas Necesarias

```bash
# macOS (usando Homebrew)
brew install awscli terraform docker git

# Windows (usando Chocolatey)
choco install awscli terraform docker-desktop git

# Linux (Ubuntu/Debian)
sudo apt-get install awscli terraform docker.io git
```

Verifica las versiones:
```bash
aws --version          # AWS CLI v2.x+
terraform version      # Terraform v1.0+
docker --version       # Docker 20.10+
git --version          # Git 2.30+
java -version          # Java 21
mvn -version           # Maven 3.8+
```

---

## Paso 2: Configurar Credenciales de AWS Localmente

### Obtener credenciales:
1. Ve a [AWS IAM Console](https://console.aws.amazon.com/iam/)
2. Crea un usuario IAM llamado `devops-pipeline-user`
3. Asigna estas políticas:
   - `AmazonEC2ContainerRegistryFullAccess`
   - `AmazonECS_FullAccess`
   - `AmazonEC2FullAccess`
   - `IAMFullAccess`
   - `VPCFullAccess`
   - `CloudWatchLogsFullAccess`
   - `AmazonS3FullAccess` (para Terraform state)

4. Genera las claves de acceso (Access Key ID y Secret Access Key)

### Configurar AWS CLI:
```bash
aws configure

# Ingresa los datos cuando lo solicite:
AWS Access Key ID: YOUR_ACCESS_KEY_ID
AWS Secret Access Key: YOUR_SECRET_ACCESS_KEY
Default region name: us-east-1
Default output format: json
```

Verifica la configuración:
```bash
aws sts get-caller-identity
```

---

## Paso 3: Desplegar Infraestructura Inicial

```bash
cd terraform

# Inicializar Terraform
terraform init

# Plan para ver qué se va a crear
terraform plan -var-file=environments/prod.tfvars -out=tfplan

# Aplicar la configuración
terraform apply tfplan

# Guardar outputs
terraform output > outputs.json
```

**Esto crea:**
- VPC con subnets públicas y privadas
- ALB (Application Load Balancer)
- ECR Repository
- ECS Cluster con 2 tasks iniciales (Fargate)
- Auto-scaling groups (2-10 tasks)
- Security groups y roles IAM

### Verificar recursos creados:
```bash
# Ver cluster ECS
aws ecs list-clusters

# Ver servicios
aws ecs list-services --cluster devops-payload-prod-cluster

# Ver repositorio ECR
aws ecr describe-repositories --repository-names devops-payload-prod
```

---

## Paso 6: Configurar GitHub Secrets

Necesitas agregar 3 secrets en tu repositorio GitHub:

### 6.1 Ve a tu repositorio → Settings → Secrets and variables → Actions

### 6.2 Crea estos secretos:

**1. AWS_ACCESS_KEY_ID**
```
Valor: Tu ACCESS_KEY_ID de AWS
```

**2. AWS_SECRET_ACCESS_KEY**
```
Valor: Tu SECRET_ACCESS_KEY de AWS
```

**3. GITHUB_TOKEN** (automático, pero verifica)
```
Valor: Generado automáticamente por GitHub
```

### Cómo crear secrets en GitHub:

```bash
# Si usas GitHub CLI
gh secret set AWS_ACCESS_KEY_ID --body "YOUR_ACCESS_KEY"
gh secret set AWS_SECRET_ACCESS_KEY --body "YOUR_SECRET_KEY"
```

---

## Paso 7: Compilar y Empaquetar la Aplicación

```bash
# En la raíz del proyecto
mvn clean package -DskipTests

# Construir imagen Docker localmente (opcional, para probar)
docker build -t devops-payload:latest .

# Probar la imagen
docker run -p 8080:8080 devops-payload:latest
```

---

## Paso 8: Primer Despliegue Manual a ECR

```bash
# Obtener URL de ECR
ECR_REPO=$(aws ecr describe-repositories \
  --repository-names devops-payload-prod \
  --query 'repositories[0].repositoryUri' \
  --output text)

echo "ECR Repository: $ECR_REPO"

# Login a ECR
aws ecr get-login-password --region us-east-1 | \
  docker login --username AWS --password-stdin $ECR_REPO

# Construir y push
docker build -t $ECR_REPO:latest .
docker push $ECR_REPO:latest

# También taggear como v1.0.0
docker tag $ECR_REPO:latest $ECR_REPO:v1.0.0
docker push $ECR_REPO:v1.0.0
```

---

## Paso 9: Hacer Git Push y Gatillar Pipeline

```bash
# Agregar cambios
git add .
git commit -m "Initial infrastructure and application setup"

# Push a rama develop (despliega a DEV)
git push origin develop

# O push a main (despliega a PROD)
git push origin main
```

### Flujo según la rama:

| Rama | Acción |
|------|--------|
| `develop` | Build → Test → Push a ECR → Deploy a DEV |
| `main` / `master` | Build → Test → Push a ECR → Deploy a PROD (con smoke tests) |
| Pull Request | Build → Test → Static Analysis (sin deploy) |
| Manual (workflow_dispatch) | Permite elegir ambiente y versión |

---

## Paso 10: Monitorear el Pipeline

### Ver logs del workflow:
1. Ve a tu repo → Actions
2. Selecciona el último workflow run
3. Expande los pasos para ver detalles

### Ver logs de ECS:
```bash
# Logs en tiempo real
aws logs tail /ecs/devops-payload-prod --follow

# Últimas 100 líneas
aws logs tail /ecs/devops-payload-prod --max-items 100
```

### Ver estado del servicio ECS:
```bash
# Ver servicios
aws ecs describe-services \
  --cluster devops-payload-prod-cluster \
  --services devops-payload-prod-service \
  --query 'services[0].[runningCount,desiredCount,deployments]'

# Ver tasks
aws ecs list-tasks \
  --cluster devops-payload-prod-cluster \
  --query 'taskArns'
```

---

## Acceder a la Aplicación

Una vez desplegada, obten la URL del ALB:

```bash
# Obtener DNS del ALB
aws elbv2 describe-load-balancers \
  --query 'LoadBalancers[0].DNSName' \
  --output text
```

### Prueba el endpoint:
```bash
curl -X POST \
  -H "X-Parse-REST-API-Key: 2f5ae96c-b558-4c7b-a590-a501ae1c3f6c" \
  -H "X-JWT-KWY: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIn0.dozjgNryP4J3jVmNHl0w5N_XgL0n3I9PlFUP0THsR8U" \
  -H "Content-Type: application/json" \
  -d '{
    "message": "This is a test",
    "to": "Juan Perez",
    "from": "Rita Asturia",
    "timeToLifeSec": 45
  }' \
  http://ALB-DNS-NAME/DevOps
```

---

## Troubleshooting

### El workflow no se ejecuta:
- Verifica que los secrets estén configurados en GitHub
- Revisa que la rama sea `main`, `master` o `develop`
- Ve a Actions y checa los logs de error

### ECR push falla:
```bash
# Verifica credenciales AWS
aws sts get-caller-identity

# Re-login a ECR
aws ecr get-login-password --region us-east-1 | \
  docker login --username AWS --password-stdin $(aws sts get-caller-identity --query Account --output text).dkr.ecr.us-east-1.amazonaws.com
```

### ECS deployment falla:
```bash
# Ver detalles del servicio
aws ecs describe-services \
  --cluster devops-payload-prod-cluster \
  --services devops-payload-prod-service

# Ver eventos del servicio
aws ecs describe-services \
  --cluster devops-payload-prod-cluster \
  --services devops-payload-prod-service \
  --query 'services[0].events[:5]'
```

### Terraform state corrupted:
```bash
# Ver state actual
terraform state list

# Hacer rollback
terraform plan -destroy
terraform apply -destroy
```

---

## Clean Up (Eliminar todo)

```bash
# Destruir infraestructura
cd terraform
terraform destroy -var-file=environments/prod.tfvars

# El estado de Terraform se guarda localmente en: terraform/terraform.tfstate
# Si lo necesitas, puedes versionarlo en git o ignorarlo con .gitignore
```

---

## Preguntas Frecuentes

**P: ¿Necesito credenciales de AWS en mi máquina?**
R: Sí, para deployar la infraestructura inicial y que Terraform pueda administrarla.

**P: ¿Cómo funcionan los GitHub Secrets?**
R: Se encriptan y solo están disponibles dentro de los workflows. No se muestran en los logs públicamente.

**P: ¿Qué pasa si pierdo mis credenciales de AWS?**
R: Puedes generar nuevas en IAM y actualizar los GitHub Secrets. Los números de cuenta de AWS no cambian.

**P: ¿Puedo desplegar versiones específicas?**
R: Sí, usa el workflow manual (workflow_dispatch) y especifica el tag de imagen.

**P: ¿Cómo escalo la aplicación?**
R: Auto-scaling ya está configurado. Aumenta `max_capacity` en las tfvars.

