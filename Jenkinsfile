pipeline {
  agent any
  tools {
    jdk 'jdk-17'       
    maven 'maven-3.9.6'
  }
  options {
    buildDiscarder(logRotator(numToKeepStr: '10'))
    disableConcurrentBuilds()
    timestamps()
  }

  environment {
    SPRING_PROFILES_ACTIVE = 'ci'
  }

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Descubrir módulos') {
      steps {
        script {
          // Busca todos los pom.xml (excepto en target/)
          def poms = findFiles(glob: '**/pom.xml').findAll { !it.path.contains('\\target\\') && !it.path.contains('/target/') }
          if (poms.isEmpty()) {
            error 'No se han encontrado pom.xml en el repositorio.'
          }
          // Construye un mapa de stages en paralelo, uno por módulo
          def builds = [:]
          for (p in poms) {
            def pomPath = p.path.replace('\\','/')
            def moduleDir = pomPath.substring(0, pomPath.lastIndexOf('/'))
            builds[moduleDir] = {
              stage("Build & Test: ${moduleDir}") {
                dir(moduleDir) {
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
    always { echo "Resultado: ${currentBuild.currentResult}" }
  }
}

