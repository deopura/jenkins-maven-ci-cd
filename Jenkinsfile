pipeline {
    agent any

    environment {
        MAVEN_HOME = tool 'Maven3'
        SONARQUBE_SERVER = 'SonarQube'
        NEXUS_CREDENTIALS = credentials('nexus-creds')
        TOMCAT_CREDENTIALS = credentials('tomcat-creds')
        TOMCAT_URL = 'http://tomcat-host:8080/manager/text'
    }

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/deopura/jenkins-maven-ci-cd.git', branch: 'main'
            }
        }

        stage('Code Quality - SonarQube') {
            steps {
                withSonarQubeEnv("${SONARQUBE_SERVER}") {
                    sh "${MAVEN_HOME}/bin/mvn clean verify sonar:sonar"
                }
            }
        }

        stage('Build & Deploy to Nexus') {
            steps {
                sh "${MAVEN_HOME}/bin/mvn clean deploy -DskipTests"
            }
        }

     /*   stage('Deploy to Tomcat') {
            steps {
                deploy adapters: [tomcat8(credentialsId: "${TOMCAT_CREDENTIALS}", path: '', url: "${TOMCAT_URL}")],
                       contextPath: '/myapp',
                       war: 'target/myapp.war'
            }
        }
    } 

    post {
        success {
            echo '🎉 Deployment Successful!'
        }
        failure {
            echo '❌ Build or Deployment Failed!'
        }
    } */
}
}