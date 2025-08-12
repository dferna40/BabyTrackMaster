pipeline {
  agent any

  tools { 
    jdk 'jdk-17'
    maven 'maven-3.9.6'
  }

  options { 
    buildDiscarder(logRotator(numToKeepStr: '15'))
    disableConcurrentBuilds()
    timestamps()
  }

  environment {
    SPRING_PROFILES_ACTIVE = 'ci'
    // Se completa tras elegir puerto
    SPRING_CLOUD_CONFIG_URI = "http://localhost:${CONFIG_SERVER_PORT}"
  }

  stages {

    stage('Checkout') { steps { checkout scm } }

    stage('Elegir puerto libre') {
      steps {
        powershell '''
          $listener = New-Object System.Net.Sockets.TcpListener([System.Net.IPAddress]::Loopback,0)
          $listener.Start()
          $port = $listener.LocalEndpoint.Port
          $listener.Stop()
          New-Item -ItemType Directory -Force -Path "config-server/target" | Out-Null
          Set-Content -Path "config-server/target/port.txt" -Value $port
          Write-Host "CONFIG_SERVER_PORT=$port"
        '''
        script { env.CONFIG_SERVER_PORT = readFile('config-server/target/port.txt').trim() }
      }
    }

    stage('Empaquetar config-server') {
      steps { dir('config-server') { bat 'mvn -B -U -DskipTests package' } }
    }

    stage('Arrancar config-server') {
      steps {
        dir('config-server') {
          bat """
          mvn -B -U spring-boot:start ^
            -Dspring-boot.run.profiles=%SPRING_PROFILES_ACTIVE% ^
            -Dspring-boot.run.arguments="--server.port=%CONFIG_SERVER_PORT%" ^
            -Dspring-boot.run.jvmArguments="-Dspring.profiles.active=%SPRING_PROFILES_ACTIVE% -Dspring-boot.pid.file=target/config-server.pid"
          """
        }
      }
    }

    stage('Esperar a que esté UP') {
      steps {
        powershell '''
          $port = $env:CONFIG_SERVER_PORT
          $deadline = (Get-Date).AddMinutes(4)
          $ok = $false

          function TryUrl([string]$url){
            try {
              $resp = Invoke-WebRequest -UseBasicParsing -Uri $url -TimeoutSec 5
              $code = [int]$resp.StatusCode
              $body = $resp.Content
              $preview = if ($null -ne $body) { $body.Substring(0, [Math]::Min(200, $body.Length)) } else { "" }
              Write-Host ("URL: " + $url)
              Write-Host ("CODE: " + $code)
              Write-Host ("BODY: " + $preview)
              return @{ ok = ($code -ge 200 -and $code -lt 300); code = $code; body = $body }
            } catch {
              Write-Host ("URL: " + $url)
              Write-Host ("ERROR: " + $_.Exception.Message)
              return @{ ok = $false; code = 0; body = "" }
            }
          }

          while(-not $ok -and (Get-Date) -lt $deadline) {
            # 1) Actuator health (si está expuesto)
            $r = TryUrl ("http://localhost:" + $port + "/actuator/health")
            if ($r.ok -and $r.body -match '"status"\\s*:\\s*"UP"') { $ok = $true; break }

            # 2) Fallback: endpoint propio del Config Server
            $r2 = TryUrl ("http://localhost:" + $port + "/application/default")
            if ($r2.ok) { $ok = $true; break }

            Start-Sleep -Seconds 2
          }

          if (-not $ok) { throw "Config-server no respondió dentro del tiempo (puerto $port)" }
        '''
      }
    }

    stage('Build & Test (módulos)') {
      steps {
        script {
          // Ajusta la lista de módulos según tus carpetas con pom.xml
          def modules = [
            'api-citas','api-cuidados','api-diario','api-gastos',
            'api-gateway','api-hitos','api-rutinas','api-usuarios'
          ]
          def builds = [:]
          modules.each { m ->
            builds[m] = {
              stage("Build & Test: ${m}") {
                dir(m) {
                  bat 'mvn -B -U clean verify'
                }
              }
            }
          }
          parallel builds
        }
      }
    }

    stage('Reportes') {
      steps {
        junit '**/target/surefire-reports/*.xml'
        archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
      }
    }
  }

  post {
    always {
      script {
        try {
          dir('config-server') {
            bat 'mvn -B -U -Dspring-boot.stop.pidFile=target/config-server.pid spring-boot:stop'
          }
        } catch (e) {
          echo "Stop por Maven falló; intento matar por PID…"
          bat 'for /f %a in (config-server\\target\\config-server.pid) do taskkill /PID %a /F'
        }
      }
      echo "Resultado: ${currentBuild.currentResult}"
    }
  }
}
