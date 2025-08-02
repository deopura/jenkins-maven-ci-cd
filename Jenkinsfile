pipeline {
    agent any

    environment {
        MAVEN_HOME = tool 'Maven3'
        SONARQUBE_SERVER = 'SonarQube'
        NEXUS_CREDENTIALS = credentials('nexus-creds')
        TOMCAT_CREDENTIALS = credentials('tomcat-creds')
        TOMCAT_URL = 'http://localhost:9080/manager/text'
        TOMCAT_HOST = "http://localhost:9080"
        // WAR_NAME = "${APP_NAME}-${VERSION}.war"
    }

    stages {
       stage('Clean Workspace') {
            steps {
                cleanWs()
            }
        }
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

        stage('Build') {
            steps {
            sh "${MAVEN_HOME}/bin/mvn clean package -DskipTests"
            }
        }

        stage('Upload to Nexus') {
            steps {
        withCredentials([usernamePassword(credentialsId: 'nexus-creds', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')]) {
            sh """
                curl -v -u $NEXUS_USER:$NEXUS_PASS \
                --upload-file target/myapp-1.0.0.war \
                http://localhost:8081/repository/maven-releases/com/example/myapp/1.0.0/myapp-1.0.0.war
            """
        }
        }
        }   

        stage('Deploy to Tomcat'){
        steps {
        withCredentials([usernamePassword(credentialsId: 'tomcat-creds', usernameVariable: 'TOMCAT_USER', passwordVariable: 'TOMCAT_PASS')]) 
        {
          sh "curl -v --upload-file target/myapp-1.0.0.war '${TOMCAT_HOST}/manager/text/deploy?path=/myapp&update=true' --user $TOMCAT_USER:$TOMCAT_PASS"
        }     
        }
         }
 }
}
