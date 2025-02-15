pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "elinsu/java-app"
        DOCKER_CREDENTIALS_ID = "docker-hub-credentials"
        SECOND_SERVER = "root@ikinci-server-ip"
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/NusretTinel/jenkins-pipeline'
            }
        }
        
        stage('Build & Test') {
            steps {
                sh 'javac Main.java'
                sh 'java Main'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh ' docker build -t nusrettinel/java-app -f /var/lib/jenkins/workspace/Java-Pipeline/Dockerfile .'
                
            }
        }

        stage('Push Docker Image') {
            steps {
                withDockerRegistry([credentialsId: DOCKER_CREDENTIALS_ID, url: '']) {
                    sh 'docker push $DOCKER_IMAGE'
                }
            }
        }

        stage('Deploy to Second Server') {
            steps {
                sh '''
                ssh -o StrictHostKeyChecking=no $SECOND_SERVER '
                    docker pull $DOCKER_IMAGE &&
                    docker stop java_app || true &&
                    docker rm java_app || true &&
                    docker run -d --name java_app -p 8080:8080 $DOCKER_IMAGE
                '
                '''
            }
        }
    }
}

