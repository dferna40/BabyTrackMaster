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
  stages {
    stage('Checkout') {
      steps { checkout scm }
    }
    stage('Build & Test') {
      steps {
        withEnv(['SPRING_PROFILES_ACTIVE=ci']) {
          bat 'mvn -B -U clean verify'
        }
      }
    }
    stage('Reportes') {
      steps {
        junit 'target/surefire-reports/*.xml'
        archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
      }
    }
  }
  post {
    always { echo "Resultado: ${currentBuild.currentResult}" }
  }
}
