# PowerShell Script para Deploy Manual a ECR
# Uso: .\scripts\deploy-to-ecr.ps1 -Environment prod -Version v1.0.0

param(
    [Parameter(Mandatory = $false)]
    [string]$Environment = "prod",
    
    [Parameter(Mandatory = $false)]
    [string]$Version = "latest",
    
    [Parameter(Mandatory = $false)]
    [string]$Region = "us-east-1"
)

# Colores para output
$GREEN = "`e[32m"
$RED = "`e[31m"
$YELLOW = "`e[33m"
$BLUE = "`e[34m"
$RESET = "`e[0m"

# Funciones
function Write-Header {
    param([string]$Message)
    Write-Host ""
    Write-Host "============================================" -ForegroundColor Cyan
    Write-Host "$Message" -ForegroundColor Cyan
    Write-Host "============================================" -ForegroundColor Cyan
    Write-Host ""
}

function Write-Success {
    param([string]$Message)
    Write-Host "✓ $Message" -ForegroundColor Green
}

function Write-Error {
    param([string]$Message)
    Write-Host "✗ $Message" -ForegroundColor Red
}

function Write-Warning {
    param([string]$Message)
    Write-Host "⚠ $Message" -ForegroundColor Yellow
}

function Write-Info {
    param([string]$Message)
    Write-Host "ℹ $Message" -ForegroundColor Blue
}

# Validar dependencias
function Test-Dependencies {
    Write-Header "Validando Dependencias"
    
    $missingDeps = @()
    
    # Verificar Docker
    try {
        $dockerVersion = docker --version
        Write-Success "Docker: $dockerVersion"
    }
    catch {
        $missingDeps += "Docker"
        Write-Error "Docker no encontrado"
    }
    
    # Verificar AWS CLI
    try {
        $awsVersion = aws --version
        Write-Success "AWS CLI: $awsVersion"
    }
    catch {
        $missingDeps += "AWS CLI"
        Write-Error "AWS CLI no encontrado"
    }
    
    if ($missingDeps.Count -gt 0) {
        Write-Error "Dependencias faltantes: $($missingDeps -join ', ')"
        exit 1
    }
}

# Verificar credenciales de AWS
function Test-AWSCredentials {
    Write-Header "Verificando Credenciales de AWS"
    
    try {
        $identity = aws sts get-caller-identity --region $Region | ConvertFrom-Json
        Write-Success "Credenciales válidas"
        Write-Info "Account: $($identity.Account)"
        Write-Info "ARN: $($identity.Arn)"
        return $identity.Account
    }
    catch {
        Write-Error "No se pudo verificar credenciales de AWS"
        Write-Error "Asegúrate de ejecutar: aws configure"
        exit 1
    }
}

# Obtener URL de ECR
function Get-ECRRepository {
    param([string]$AccountId)
    
    Write-Header "Obteniendo Repositorio ECR"
    
    $repositoryName = "devops-payload-$Environment"
    
    try {
        $repo = aws ecr describe-repositories `
            --repository-names $repositoryName `
            --region $Region `
            --query 'repositories[0].repositoryUri' `
            --output text
        
        if ($repo -and $repo -ne "None") {
            Write-Success "Repositorio encontrado: $repo"
            return $repo
        }
        else {
            Write-Error "Repositorio no encontrado: $repositoryName"
            Write-Info "Crea el repositorio con Terraform primero"
            exit 1
        }
    }
    catch {
        Write-Error "Error obteniendo repositorio: $_"
        exit 1
    }
}

# Login a ECR
function Connect-ECR {
    param([string]$RepositoryUrl, [string]$Region)
    
    Write-Header "Login a Amazon ECR"
    
    try {
        Write-Info "Obteniendo credenciales de ECR..."
        aws ecr get-login-password --region $Region | docker login --username AWS --password-stdin $RepositoryUrl
        Write-Success "Login exitoso"
    }
    catch {
        Write-Error "Error en login a ECR: $_"
        exit 1
    }
}

# Compilar aplicación
function Build-Application {
    Write-Header "Compilando Aplicación"
    
    try {
        Write-Info "Ejecutando Maven package..."
        
        
        if ($LASTEXITCODE -ne 0) {
            Write-Error "Error en la compilación de Maven"
            exit 1
        }
        
        Write-Success "Aplicación compilada exitosamente"
    }
    catch {
        Write-Error "Error compilando: $_"
        exit 1
    }
}

# Build Docker image
function Build-DockerImage {
    param([string]$RepositoryUrl, [string]$Version)
    
    Write-Header "Construyendo Imagen Docker"
    
    $imageTag = "$($RepositoryUrl):$Version"
    
    try {
        Write-Info "Build: $imageTag"
        docker build -t $imageTag -f Dockerfile .
        
        if ($LASTEXITCODE -ne 0) {
            Write-Error "Error en docker build"
            exit 1
        }
        
        Write-Success "Imagen Docker construida: $imageTag"
    }
    catch {
        Write-Error "Error en docker build: $_"
        exit 1
    }
}

# Push Docker image
function Push-DockerImage {
    param([string]$RepositoryUrl, [string]$Version)
    
    Write-Header "Enviando Imagen a ECR"
    
    $imageTag = "$($RepositoryUrl):$Version"
    
    try {
        Write-Info "Push: $imageTag"
        docker push $imageTag
        
        if ($LASTEXITCODE -ne 0) {
            Write-Error "Error en docker push"
            exit 1
        }
        
        Write-Success "Imagen enviada: $imageTag"
    }
    catch {
        Write-Error "Error en docker push: $_"
        exit 1
    }
}

# Tag adicional con 'latest' si no es latest
function Tag-LatestImage {
    param([string]$RepositoryUrl, [string]$Version)
    
    if ($Version -ne "latest") {
        Write-Header "Creando Tag Latest"
        
        $sourceTag = "$($RepositoryUrl):$Version"
        $latestTag = "$($RepositoryUrl):latest"
        
        try {
            Write-Info "Tag: $sourceTag → $latestTag"
            docker tag $sourceTag $latestTag
            docker push $latestTag
            Write-Success "Tag 'latest' actualizado"
        }
        catch {
            Write-Warning "Error creando tag latest: $_"
        }
    }
}

# Verificar imagen en ECR
function Verify-ECRImage {
    param([string]$RepositoryName, [string]$Version)
    
    Write-Header "Verificando Imagen en ECR"
    
    try {
        $images = aws ecr describe-images `
            --repository-name $RepositoryName `
            --region $Region `
            --query "imageDetails[?contains(imageTags, '$Version')].imageTags" `
            --output json | ConvertFrom-Json
        
        if ($images -and $images.Count -gt 0) {
            Write-Success "Imagen verificada en ECR"
            Write-Info "Tags: $($images -join ', ')"
        }
        else {
            Write-Warning "No se encontró la imagen con tag $Version"
        }
    }
    catch {
        Write-Warning "Error verificando imagen: $_"
    }
}

# Main flow
function Main {
    Clear-Host
    
    Write-Host ""
    Write-Host "╔════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
    Write-Host "║     Deploy Manual a AWS ECR - DevOps Payload           ║" -ForegroundColor Cyan
    Write-Host "╚════════════════════════════════════════════════════════╝" -ForegroundColor Cyan
    Write-Host ""
    
    Write-Info "Parámetros:"
    Write-Info "  Environment: $Environment"
    Write-Info "  Version: $Version"
    Write-Info "  Region: $Region"
    Write-Host ""
    
    # Ejecutar pasos
    Test-Dependencies
    $accountId = Test-AWSCredentials
    $repositoryUrl = Get-ECRRepository -AccountId $accountId
    Connect-ECR -RepositoryUrl $repositoryUrl -Region $Region
    Build-Application
    Build-DockerImage -RepositoryUrl $repositoryUrl -Version $Version
    Push-DockerImage -RepositoryUrl $repositoryUrl -Version $Version
    Tag-LatestImage -RepositoryUrl $repositoryUrl -Version $Version
    Verify-ECRImage -RepositoryName "devops-payload-$Environment" -Version $Version
    
    Write-Header "¡Deployment Completado!"
    Write-Success "Imagen disponible en:"
    Write-Info "  $repositoryUrl`:$Version"
    
    Write-Host ""
    Write-Info "Próximos pasos:"
    Write-Info "1. Esperar a que ECS tire las tasks viejas"
    Write-Info "2. ECS lanzará nuevas tasks con la imagen actualizada"
    Write-Info "3. Monitorear en CloudWatch:"
    Write-Info "   aws logs tail /ecs/devops-payload-$Environment --follow"
    Write-Host ""
}

# Ejecutar main
Main

