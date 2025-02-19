pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "nusrettinel/java-app"
        DOCKER_USERNAME = "nusrettinel"
        DOCKER_PASSWORD = credentials('docker-hub-password')  // Docker Hub Credentials ID
        SECOND_SERVER = "root@ikinci-server-ip"
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
                javac Main.java
                java Main
                '''
            }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
                docker build -t "${env.DOCKER_IMAGE}" -f "${env.WORKSPACE}/Dockerfile" .
                '''
            }
        }

        stage('Push Docker Image') {
            steps {
                sh '''
                echo "${DOCKER_PASSWORD}" | docker login -u "${DOCKER_USERNAME}" --password-stdin
                docker push "${env.DOCKER_IMAGE}"
                '''
            }
        }

        stage('Deploy to Second Server') {
            steps {
                sh """
                ssh -o StrictHostKeyChecking=no "${env.SECOND_SERVER}" << EOF
                    docker pull "${env.DOCKER_IMAGE}"
                    docker stop java_app || true
                    docker rm java_app || true
                    docker run -d --name java_app -p 8443:8443 "${env.DOCKER_IMAGE}"
                EOF
                """
            }
        }
    }
}
