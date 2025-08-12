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
    // Los clientes del config server usarán este URI cuando arranque (se completa con el puerto dinámico)
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
          // Arranca en background con puerto dinámico y guarda el PID
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

          $pidFile = "config-server/target/config-server.pid"
          if (Test-Path $pidFile) {
            $pid = Get-Content $pidFile
            Write-Host "PID leido: $pid"
          } else {
            Write-Host "Aún no existe el PID file…"
          }

          function PortListening([int]$p){
            try { Test-NetConnection -ComputerName localhost -Port $p -InformationLevel Quiet } catch { $false }
          }

          $curl = "$env:SystemRoot\\System32\\curl.exe"
          if (-not (Test-Path $curl)) { $curl = "curl" }  # fallback

          function TryCurl([string]$url){
            try {
              $psi = New-Object System.Diagnostics.ProcessStartInfo
              $psi.FileName = $curl
              $psi.Arguments = "-s -o - -w `%{http_code`% `"$url`""
              $psi.RedirectStandardOutput = $true
              $psi.UseShellExecute = $false
              $p = [System.Diagnostics.Process]::Start($psi)
              $out = $p.StandardOutput.ReadToEnd()
              $p.WaitForExit()
              if ($out.Length -ge 3) {
                $code = $out.Substring($out.Length-3)
                $body = $out.Substring(0, $out.Length-3)
                return @{ ok = ($code -like "2*"); code = $code; body = $body }
              } else { return @{ ok = $false; code = "000"; body = "" } }
            } catch { return @{ ok = $false; code = "000"; body = "" } }
          }

          while(-not $ok -and (Get-Date) -lt $deadline) {
            if (PortListening $port) {
              Write-Host "Puerto $port en escucha; probando /actuator/health…"
              $r = TryCurl ("http://localhost:"+$port+"/actuator/health")
              Write-Host ("HEALTH http " + $r.code)
              if ($r.ok -and $r.body -match '"status"\\s*:\\s*"UP"') { $ok = $true; break }

              Write-Host "Probando /application/default…"
              $r2 = TryCurl ("http://localhost:"+$port+"/application/default")
              Write-Host ("APP/DEFAULT http " + $r2.code)
              if ($r2.ok) { $ok = $true; break }
            } else {
              Write-Host "Aún no hay nadie escuchando en $port…"
            }
            Start-Sleep -Seconds 2
          }

          if (-not $ok) { throw "Config-server no respondió en 4 minutos (puerto $port)" }
        '''
      }
    }

    stage('Build & Test (módulos)') {
      steps {
        script {
          // Añade/quita módulos Maven aquí (carpetas que contienen pom.xml)
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
          echo "Stop vía Maven falló, intento matar el proceso por PID…"
          bat 'for /f %a in (config-server\\target\\config-server.pid) do taskkill /PID %a /F'
        }
      }
      echo "Resultado: ${currentBuild.currentResult}"
    }
  }
}
