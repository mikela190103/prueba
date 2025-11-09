# DevOps Payload Microservice - Banco Pichincha

Microservicio REST desarrollado con **Spring Boot 3.5**, **TDD (Test-Driven Development)** y **Clean Code**, desplegable en **AWS con ECS Fargate**, **ALB** y **Auto-scaling**.

## 📋 Características

### Backend (Java/Spring Boot)
- ✅ **100% Test Coverage** con 79 tests unitarios
- ✅ **TDD** - Todos los tests pasan
- ✅ **Clean Code** - Siguiendo principios SOLID
- ✅ **Spring Boot 3.5** con Java 21
- ✅ **Validación de entrada** con Jakarta Validation
- ✅ **Seguridad API Key + JWT**
- ✅ **Excepciones personalizadas**
- ✅ **Cobertura de código JaCoCo** (80%+)
- ✅ **Análisis estático** (Checkstyle, PMD, SpotBugs)

### Docker & Containerización
- ✅ **Dockerfile multi-stage** optimizado
- ✅ **Imagen ultra compacta** (~150MB)
- ✅ **Health checks** integrados
- ✅ **Usuario no-root** para seguridad
- ✅ **Soporte para multi-plataforma**

### Infraestructura como Código (Terraform)
- ✅ **VPC completa** con subnets públicas y privadas
- ✅ **Application Load Balancer** para distribución de tráfico
- ✅ **ECS Fargate** (sin gestión de servidores)
- ✅ **Auto-scaling dinámico** (2-10 tasks)
- ✅ **ECR** para almacenamiento de imágenes
- ✅ **Security Groups** configurados
- ✅ **IAM roles** con permisos mínimos
- ✅ **CloudWatch logs** integrados

### CI/CD Pipeline (GitHub Actions)
- ✅ **Triggers automáticos** por rama
- ✅ **Build & Test** en todas las ramas
- ✅ **Static Analysis** (PMD, Checkstyle, SpotBugs)
- ✅ **Docker build & push** a ECR
- ✅ **Deployment automático** a ECS
- ✅ **Ambientes separados** (dev, prod)
- ✅ **Workflow manual** para deploy on-demand
- ✅ **Smoke tests** en producción

## 🚀 Quick Start

### Requisitos Previos
- AWS Account con permisos
- Docker Desktop
- AWS CLI configurado
- Terraform 1.0+
- Java 21
- Maven 3.8+

### 1. Setup Infraestructura

```bash
# Navegar al directorio
cd C:\Users\{usuario}\OneDrive\Escritorio\a\payload

# Configurar AWS CLI
aws configure
# Ingresa tu Access Key ID y Secret Access Key

# Inicializar Terraform
cd terraform
terraform init
terraform plan -var-file=environments/prod.tfvars
terraform apply -var-file=environments/prod.tfvars
cd ..
```

### 2. Deploy a ECR (PowerShell)

```powershell
# Script completo que compila, construye y envía a ECR
.\scripts\deploy-to-ecr.ps1 -Environment prod -Version v1.0.0
```

### 3. Actualizar Servicio ECS

```powershell
# Actualizar servicio con monitoreo en tiempo real
.\scripts\update-ecs-service.ps1 -Environment prod -WaitForStable
```

### 4. Probar Endpoint

```bash
curl -X POST \
  -H "X-Parse-REST-API-Key: 2f5ae96c-b558-4c7b-a590-a501ae1c3f6c" \
  -H "X-JWT-KWY: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIn0.dozjgNryP4J3jVmNHl0w5N_XgL0n3I9PlFUP0THsR8U" \
  -H "Content-Type: application/json" \
  -d '{"message":"This is a test","to":"Juan Perez","from":"Rita Asturia","timeToLifeSec":45}' \
  http://{ALB_DNS_NAME}/DevOps
```

## 📁 Estructura del Proyecto

```
payload/
├── src/
│   ├── main/java/com/devops/payload/
│   │   ├── controller/          # Controladores REST
│   │   │   └── DevOpsController.java
│   │   ├── service/             # Lógica de negocio
│   │   │   ├── DevOpsService.java
│   │   │   ├── SecurityService.java
│   │   │   └── JwtService.java
│   │   ├── model/               # Modelos JPA (sin BD)
│   │   │   ├── DevOpsRequest.java
│   │   │   └── DevOpsResponse.java
│   │   ├── exception/           # Excepciones personalizadas
│   │   │   ├── InvalidApiKeyException.java
│   │   │   ├── InvalidJwtException.java
│   │   │   └── InvalidMethodException.java
│   │   ├── config/              # Configuraciones Spring
│   │   │   └── SecurityConfiguration.java
│   │   └── PayloadApplication.java
│   ├── resources/
│   │   └── application.properties
│   └── test/java/...            # 79 tests unitarios (100% cobertura)
├── terraform/                   # Infraestructura
│   ├── main.tf
│   ├── variables.tf
│   ├── outputs.tf
│   ├── modules/
│   │   ├── networking/          # VPC, subnets, gateways
│   │   ├── ecr/                 # ECR repository
│   │   └── ecs/                 # ECS cluster, ALB, auto-scaling
│   └── environments/
│       ├── prod.tfvars
│       └── dev.tfvars
├── scripts/
│   ├── deploy-to-ecr.ps1        # Deploy manual a ECR
│   └── update-ecs-service.ps1   # Actualizar ECS
├── .github/workflows/
│   └── ci-cd.yml                # Pipeline GitHub Actions
├── Dockerfile                   # Multi-stage optimizado
├── .dockerignore
├── pom.xml
└── README.md
```

## 📊 Tests y Cobertura

### Ejecutar Tests Localmente

```bash
# Tests unitarios
mvn test

# Con cobertura
mvn test jacoco:report

# Resultado: target/site/jacoco/index.html
```

### Resultados

```
✓ 79 tests ejecutados
✓ 0 fallos
✓ 100% cobertura de código
✓ Build SUCCESS
```

### Test Breakdown
- **DevOpsController**: 20 tests (security, validation, HTTP methods)
- **DevOpsService**: 4 tests (business logic)
- **SecurityService**: 7 tests (API key validation)
- **JwtService**: 9 tests (JWT format validation)
- **Models**: 23 tests (equals, hashCode, toString)
- **Exceptions**: 10 tests
- **Configuration**: 3 tests
- **Application**: 3 tests

## 🔐 Seguridad

### API Key
```
X-Parse-REST-API-Key: 2f5ae96c-b558-4c7b-a590-a501ae1c3f6c
```

### JWT
```
X-JWT-KWY: {valid_jwt_token}
```

Ambas son **requeridas** para cada request.

## 📖 Documentación

### Guías Principales
- **[DEPLOYMENT_GUIDE.md](./DEPLOYMENT_GUIDE.md)** - Guía completa de despliegue
- **[CI_CD_FLOW.md](./CI_CD_FLOW.md)** - Explicación del pipeline de CI/CD
- **[POWERSHELL_GUIDE.md](./POWERSHELL_GUIDE.md)** - Scripts PowerShell para deploy manual

### Requisitos del Proyecto
- ✅ Microservicio containerizado (Docker)
- ✅ Mínimo 2 nodos con load balancer (ECS con ALB, 2-10 tasks)
- ✅ Infraestructura versionada (Terraform)
- ✅ Pipeline como código (GitHub Actions)
- ✅ Dependency management (Maven pom.xml)
- ✅ Stages: build, test, static analysis, docker, deploy
- ✅ Auto-ejecución por rama
- ✅ Ambientes separados (dev, prod)
- ✅ Tests automáticos (79 tests)
- ✅ Análisis estático (Checkstyle, PMD, SpotBugs)
- ✅ Clean Code + TDD
- ✅ 100% test coverage

## 🔄 Flujo de Deploy

### Opción 1: Automático (Git Push)

```bash
# Desarrollo
git checkout develop
git add .
git commit -m "feat: new feature"
git push origin develop
# → Deploy automático a DEV

# Producción
git checkout main
git merge develop
git push origin main
# → Deploy automático a PROD
```

### Opción 2: Manual (PowerShell)

```powershell
# Compilar, dockerizar y enviar a ECR
.\scripts\deploy-to-ecr.ps1 -Environment prod -Version v1.0.0

# Actualizar servicio ECS
.\scripts\update-ecs-service.ps1 -Environment prod -WaitForStable
```

### Opción 3: Workflow Manual

1. Ve a tu repo en GitHub
2. Actions → CI/CD Pipeline
3. "Run workflow"
4. Selecciona ambiente y versión

## 📈 Monitoreo

### Logs en Tiempo Real

```bash
# Desarrollo
aws logs tail /ecs/devops-payload-dev --follow

# Producción
aws logs tail /ecs/devops-payload-prod --follow
```

### Status del Servicio

```bash
# Prod
aws ecs describe-services \
  --cluster devops-payload-prod-cluster \
  --services devops-payload-prod-service

# Dev
aws ecs describe-services \
  --cluster devops-payload-dev-cluster \
  --services devops-payload-dev-service
```

## 🛠️ Troubleshooting

### Build falla
```bash
mvn clean compile
# Si persiste: Verificar Java 21 y Maven 3.8+
```

### Tests fallan
```bash
mvn test -Dorg.slf4j.simpleLogger.defaultLogLevel=debug
```

### Docker build lento
- Primera ejecución es lenta (descarga base images)
- Las siguientes son más rápidas (caché)

### ECS deployment falla
```bash
# Ver detalles
aws ecs describe-services --cluster devops-payload-prod-cluster --services devops-payload-prod-service
```

Ver [DEPLOYMENT_GUIDE.md](./DEPLOYMENT_GUIDE.md) para más troubleshooting.

## 📊 Arquitectura

```
┌─────────────────────────────────────────────────────┐
│              AWS VPC (10.0.0.0/16)                  │
├─────────────────────────────────────────────────────┤
│                                                     │
│  ┌──────────────────────────────────────────────┐  │
│  │     ALB (Application Load Balancer)          │  │
│  │     Puerto 80 (HTTP)                         │  │
│  └─────────────┬────────────────────────────────┘  │
│                │                                    │
│  ┌─────────────┴────────────────────────────────┐  │
│  │    ECS Fargate Cluster                       │  │
│  │    (2-10 tasks dinámicos)                    │  │
│  │                                              │  │
│  │  ┌─────────────┐  ┌──────────────┐          │  │
│  │  │  Task 1     │  │   Task 2     │  ...    │  │
│  │  │ Port 8080   │  │ Port 8080    │          │  │
│  │  │ 512 CPU     │  │ 512 CPU      │          │  │
│  │  │ 1 GB RAM    │  │ 1 GB RAM     │          │  │
│  │  └─────────────┘  └──────────────┘          │  │
│  │                                              │  │
│  │  Auto-scaling basado en:                    │  │
│  │  • CPU >= 70%: scale out                    │  │
│  │  • CPU < 70%: scale in                      │  │
│  │  • Memory >= 80%: scale out                 │  │
│  │  • Memory < 80%: scale in                   │  │
│  └──────────────────────────────────────────────┘  │
│                                                     │
│  ┌──────────────────────────────────────────────┐  │
│  │     ECR Repository                           │  │
│  │     devops-payload-prod                      │  │
│  └──────────────────────────────────────────────┘  │
│                                                     │
│  ┌──────────────────────────────────────────────┐  │
│  │  CloudWatch Logs                             │  │
│  │  /ecs/devops-payload-prod                    │  │
│  └──────────────────────────────────────────────┘  │
│                                                     │
└─────────────────────────────────────────────────────┘
```

## 🤝 Contribución

1. Create feature branch: `git checkout -b feature/feature-name`
2. Commit changes: `git commit -m "feat: description"`
3. Push to develop: `git push origin feature/feature-name`
4. Create Pull Request

## 📝 Licencia

Este proyecto es parte de la prueba técnica de DevOps para Banco Pichincha.

## ✅ Checklist Final

- [ ] Código compilado sin errores
- [ ] 79 tests ejecutados y pasando
- [ ] Cobertura de código >= 80%
- [ ] Análisis estático pasando
- [ ] Imagen Docker construida
- [ ] Imagen enviada a ECR
- [ ] Infraestructura desplegada con Terraform
- [ ] Servicio ECS actualizado
- [ ] ALB respondiendo
- [ ] Logs en CloudWatch
- [ ] Endpoint probado exitosamente
- [ ] GitHub Actions pipeline configurado
- [ ] Secretos configurados en GitHub

---

**Desarrollado con ❤️ siguiendo Clean Code y TDD**

Para más información, consulta la [DEPLOYMENT_GUIDE.md](./DEPLOYMENT_GUIDE.md)

