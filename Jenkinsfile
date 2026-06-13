pipeline {
  agent any

  triggers {
    pollSCM('H/5 * * * *')
  }

  stages {
    stage('Install Frontend Dependencies') {
      steps {
        dir('Scout-front') {
          sh 'npm ci --include=dev'
        }
      }
    }

    stage('Backend Tests') {
      steps {
        dir('Scout5') {
          sh 'chmod +x ./mvnw'
          sh './mvnw test'
        }
      }
    }

    stage('Project Configuration Validation') {
      steps {
        sh 'test -f docker-compose.yml'
        sh 'test -f .env.example'
        sh 'test -f Scout5/pom.xml'
        sh 'test -f Scout-front/package.json'
      }
    }

    stage('Build Backend') {
      steps {
        dir('Scout5') {
          sh 'chmod +x ./mvnw'
          sh './mvnw package -DskipTests'
        }
      }
    }

    stage('Build Frontend') {
      steps {
        dir('Scout-front') {
          sh 'npm run build'
        }
      }
    }

    stage('Package Artifacts') {
      steps {
        archiveArtifacts artifacts: 'Scout5/target/*.jar, Scout-front/dist/**', fingerprint: true
      }
    }

    stage('Deployment Simulation') {
      steps {
        sh 'test -f Scout5/Dockerfile'
        sh 'test -f Scout-front/Dockerfile'
        echo 'Deployment simulation command: docker compose --env-file .env.example up --build -d'
        echo 'Deployment simulation completed: compose file and Dockerfiles are available.'
      }
    }
  }

  post {
    success {
      echo 'Pipeline finished successfully: dependencies, tests, validation, builds, artifacts and deployment simulation passed.'
    }
    failure {
      echo 'Pipeline failed. Check the stage logs to identify the failed CI/CD step.'
    }
    always {
      echo "Pipeline status: ${currentBuild.currentResult}"
    }
  }
}
