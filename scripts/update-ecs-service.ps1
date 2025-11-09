# PowerShell Script para Actualizar Servicio ECS
# Uso: .\scripts\update-ecs-service.ps1 -Environment prod

param(
    [Parameter(Mandatory = $false)]
    [string]$Environment = "prod",
    
    [Parameter(Mandatory = $false)]
    [string]$Region = "us-east-1",
    
    [Parameter(Mandatory = $false)]
    [switch]$WaitForStable = $false
)

# Colores para output
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

# Obtener información del servicio
function Get-ServiceInfo {
    Write-Header "Obteniendo Información del Servicio"
    
    $clusterName = "devops-payload-$Environment-cluster"
    $serviceName = "devops-payload-$Environment-service"
    
    try {
        Write-Info "Cluster: $clusterName"
        Write-Info "Service: $serviceName"
        
        $service = aws ecs describe-services `
            --cluster $clusterName `
            --services $serviceName `
            --region $Region `
            --query 'services[0]' | ConvertFrom-Json
        
        if ($service) {
            Write-Success "Servicio encontrado"
            Write-Info "Estado: $($service.status)"
            Write-Info "Tasks deseadas: $($service.desiredCount)"
            Write-Info "Tasks corriendo: $($service.runningCount)"
            
            return @{
                ClusterName = $clusterName
                ServiceName = $serviceName
                Service = $service
            }
        }
        else {
            Write-Error "Servicio no encontrado"
            exit 1
        }
    }
    catch {
        Write-Error "Error obteniendo servicio: $_"
        exit 1
    }
}

# Actualizar servicio ECS
function Update-ECSService {
    param(
        [string]$ClusterName,
        [string]$ServiceName
    )
    
    Write-Header "Actualizando Servicio ECS"
    
    try {
        Write-Info "Forzando nuevo despliegue..."
        aws ecs update-service `
            --cluster $ClusterName `
            --service $ServiceName `
            --force-new-deployment `
            --region $Region | Out-Null
        
        Write-Success "Comando de actualización enviado"
    }
    catch {
        Write-Error "Error actualizando servicio: $_"
        exit 1
    }
}

# Esperar a que el servicio sea estable
function Wait-ServiceStable {
    param(
        [string]$ClusterName,
        [string]$ServiceName
    )
    
    Write-Header "Esperando que el servicio sea estable"
    
    try {
        Write-Info "Esto puede tomar algunos minutos..."
        Write-Info "Presiona Ctrl+C para cancelar"
        Write-Host ""
        
        aws ecs wait services-stable `
            --cluster $ClusterName `
            --services $ServiceName `
            --region $Region
        
        Write-Success "Servicio estable"
    }
    catch {
        Write-Warning "Timeout esperando que el servicio sea estable"
    }
}

# Monitorear despliegue
function Monitor-Deployment {
    param(
        [string]$ClusterName,
        [string]$ServiceName
    )
    
    Write-Header "Estado del Despliegue"
    
    for ($i = 0; $i -lt 5; $i++) {
        try {
            $service = aws ecs describe-services `
                --cluster $ClusterName `
                --services $ServiceName `
                --region $Region `
                --query 'services[0]' | ConvertFrom-Json
            
            Write-Info "Estado: $($service.status)"
            Write-Info "Tasks deseadas: $($service.desiredCount)"
            Write-Info "Tasks corriendo: $($service.runningCount)"
            Write-Info "Despliegues: $($service.deployments.Count)"
            
            if ($service.runningCount -eq $service.desiredCount -and $service.deployments.Count -eq 1) {
                Write-Success "Despliegue completado"
                break
            }
            
            $service.deployments | ForEach-Object {
                Write-Info "  - Tarea: $($_.taskDefinition -split '/')[-1]"
                Write-Info "    Estado: $($_.status)"
                Write-Info "    Count: $($_.runningCount)/$($_.desiredCount)"
            }
            
            if ($i -lt 4) {
                Write-Host ""
                Start-Sleep -Seconds 10
            }
        }
        catch {
            Write-Warning "Error monitoreando: $_"
        }
    }
}

# Ver logs en tiempo real (opcional)
function Show-Logs {
    param([string]$Environment)
    
    $logGroup = "/ecs/devops-payload-$Environment"
    
    Write-Header "Logs de CloudWatch"
    Write-Info "Mostrando últimos 50 eventos..."
    Write-Info "(Presiona Ctrl+C para salir)"
    Write-Host ""
    
    try {
        aws logs tail $logGroup --follow --region $Region
    }
    catch {
        Write-Warning "Error mostrando logs: $_"
    }
}

# Obtener URL del ALB
function Get-LoadBalancerURL {
    Write-Header "URL del Application Load Balancer"
    
    try {
        $tags = "Key=Environment,Values=$Environment", "Key=Name,Values=devops-payload-$Environment-alb"
        
        $alb = aws elbv2 describe-load-balancers `
            --region $Region `
            --query "LoadBalancers[0].DNSName" `
            --output text
        
        if ($alb -and $alb -ne "None") {
            Write-Success "ALB URL: http://$alb"
            Write-Info ""
            Write-Info "Prueba el endpoint con:"
            Write-Info "curl -X POST \"
            Write-Info "  -H 'X-Parse-REST-API-Key: 2f5ae96c-b558-4c7b-a590-a501ae1c3f6c' \"
            Write-Info "  -H 'X-JWT-KWY: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIn0.dozjgNryP4J3jVmNHl0w5N_XgL0n3I9PlFUP0THsR8U' \"
            Write-Info "  -H 'Content-Type: application/json' \"
            Write-Info "  -d '{\"message\":\"Test\",\"to\":\"User\",\"from\":\"System\",\"timeToLifeSec\":45}' \"
            Write-Info "  http://$alb/DevOps"
        }
        else {
            Write-Warning "No se encontró el ALB"
        }
    }
    catch {
        Write-Warning "Error obteniendo URL del ALB: $_"
    }
}

# Main flow
function Main {
    Clear-Host
    
    Write-Host ""
    Write-Host "╔════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
    Write-Host "║     Actualizar Servicio ECS - DevOps Payload           ║" -ForegroundColor Cyan
    Write-Host "╚════════════════════════════════════════════════════════╝" -ForegroundColor Cyan
    Write-Host ""
    
    Write-Info "Parámetros:"
    Write-Info "  Environment: $Environment"
    Write-Info "  Region: $Region"
    Write-Info "  WaitForStable: $WaitForStable"
    Write-Host ""
    
    # Ejecutar pasos
    $info = Get-ServiceInfo
    Update-ECSService -ClusterName $info.ClusterName -ServiceName $info.ServiceName
    
    Write-Host ""
    Monitor-Deployment -ClusterName $info.ClusterName -ServiceName $info.ServiceName
    
    if ($WaitForStable) {
        Wait-ServiceStable -ClusterName $info.ClusterName -ServiceName $info.ServiceName
    }
    
    Write-Host ""
    Get-LoadBalancerURL
    
    Write-Host ""
    Write-Info "¿Deseas ver los logs en tiempo real? (s/n)"
    $response = Read-Host
    
    if ($response -eq "s" -or $response -eq "S" -or $response -eq "y" -or $response -eq "Y") {
        Show-Logs -Environment $Environment
    }
    
    Write-Host ""
    Write-Success "¡Actualización completada!"
}

# Ejecutar main
Main

