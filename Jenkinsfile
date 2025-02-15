pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "nusrettinel/java-app"
        DOCKER_CREDENTIALS_ID = "docker-hub-credentials"
        SECOND_SERVER = "root@ikinci-server-ip"  // Buraya ikinci sunucunun IP'sini koy
    }

    stages {
        stage('Checkout') {
            steps {
                git credentialsId: 'github-ssh', url: 'git@github.com:NusretTinel/jenkins-pipeline.git'
            }
        }

        stage('Build & Test') {
            steps {
                sh '''
                #!/bin/bash
                javac Main.java
                java Main
                '''
            }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
                #!/bin/bash
                docker build -t "$DOCKER_IMAGE" .
                '''
            }
        }

        stage('Push Docker Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: "docker-hub-credentials", passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                    sh '''
                    #!/bin/bash
                    echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
                    docker push "$DOCKER_IMAGE"
                    '''
                }
            }
        }

        stage('Deploy to Second Server') {
            steps {
                sh '''
                #!/bin/bash
                ssh -o StrictHostKeyChecking=no $SECOND_SERVER << EOF
                    docker pull $DOCKER_IMAGE
                    docker stop java_app || true
                    docker rm java_app || true
                    docker run -d --name java_app -p 8080:8080 $DOCKER_IMAGE
                EOF
                '''
            }
        }
    }
}
