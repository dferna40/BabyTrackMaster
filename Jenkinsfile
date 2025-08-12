pipeline {
  agent any
  tools { jdk 'jdk-17'; maven 'maven-3.9.6' }
  options { buildDiscarder(logRotator(numToKeepStr: '10')); disableConcurrentBuilds(); timestamps() }

  environment {
    // Apuntar los servicios al config-server local
    SPRING_CLOUD_CONFIG_URI = 'http://localhost:8888'
    SPRING_PROFILES_ACTIVE  = 'ci'
  }

  stages {
    stage('Checkout') { steps { checkout scm } }

    stage('Empaquetar config-server') {
      steps { dir('config-server') { bat 'mvn -B -U -DskipTests package' } }
    }

    stage('Arrancar config-server') {
      steps {
        dir('config-server') {
          // Arranca en background y guarda el PID en target/config-server.pid
          bat '''
          mvn -B -U spring-boot:start ^
            -Dspring-boot.run.profiles=ci ^
            -Dspring-boot.run.jvmArguments="-Dspring.profiles.active=ci -Dspring-boot.pid.file=target/config-server.pid"
          '''
        }
      }
    }

    stage('Esperar a que esté UP') {
      steps {
        // Espera a /actuator/health (ajusta si no tienes Actuator en config-server)
        powershell '''
          $ok = $false
          $deadline = (Get-Date).AddMinutes(2)
          while(-not $ok -and (Get-Date) -lt $deadline){
            try{
              $r = Invoke-WebRequest -UseBasicParsing http://localhost:8888/actuator/health -TimeoutSec 5
              if($r.Content -match '"status":"UP"'){ $ok = $true }
            }catch{}
            if(-not $ok){ Start-Sleep -Seconds 2 }
          }
          if(-not $ok){ throw "Config-server no está UP en 2 minutos" }
        '''
      }
    }

    stage('Build & Test (módulos)') {
      steps {
        script {
          // Lista de módulos Maven (ajústala si añades/eliminas alguno)
          def modules = [
            'api-citas','api-cuidados','api-diario','api-gastos',
            'api-gateway','api-hitos','api-rutinas','api-usuarios'
          ]
          def builds = [:]
          modules.each { m ->
            builds[m] = {
              stage("Build & Test: ${m}") {
                dir(m) { bat 'mvn -B -U clean verify' }
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
      // Parar el config-server aunque falle algo
      script {
        try {
          dir('config-server') {
            bat 'mvn -B -U -Dspring-boot.stop.pidFile=target/config-server.pid spring-boot:stop'
          }
        } catch (e) {
          echo "No se pudo parar con spring-boot:stop, intento matar el proceso…"
          bat 'for /f %a in (config-server\\target\\config-server.pid) do taskkill /PID %a /F'
        }
      }
      echo "Resultado: ${currentBuild.currentResult}"
    }
  }
}
