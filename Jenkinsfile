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
        sh """
        ssh ec2-user@ec2-13-201-58-131.ap-south-1.compute.amazonaws.com "sudo systemctl stop tomcat"
        cd target
        scp myapp-1.0.0.war ec2-user@ec2-13-201-58-131.ap-south-1.compute.amazonaws.com:/opt/tomcat/webapps/
        ssh ec2-user@ec2-13-201-58-131.ap-south-1.compute.amazonaws.com "sudo systemctl start tomcat"
        ssh ec2-user@ec2-65-0-3-76.ap-south-1.compute.amazonaws.com "sudo systemctl stop tomcat"
        scp myapp-1.0.0.war ec2-user@ec2-65-0-3-76.ap-south-1.compute.amazonaws.com:/opt/tomcat/webapps/
        ssh ec2-user@ec2-65-0-3-76.ap-south-1.compute.amazonaws.com "sudo systemctl start tomcat"
        ssh ec2-user@ec2-3-109-201-29.ap-south-1.compute.amazonaws.com "sudo systemctl stop tomcat"
        scp myapp-1.0.0.war ec2-user@ec2-3-109-201-29.ap-south-1.compute.amazonaws.com:/opt/tomcat/webapps/
        ssh ec2-user@ec2-3-109-201-29.ap-south-1.compute.amazonaws.com "sudo systemctl start tomcat"
        """
        }
         }
 }
}
