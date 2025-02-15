pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "nusrettinel/java-app"
        DOCKER_CREDENTIALS_ID = "docker-hub-credentials"
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
                withCredentials([usernamePassword(credentialsId: "$DOCKER_CREDENTIALS_ID", passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                    sh '''
                    #!/bin/bash
                    echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
                    docker push "$DOCKER_IMAGE"
                    '''
                }
            }
        }
    }
}
