pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git credentialsId: 'github-ssh', url: 'git@github.com:NusretTinel/jenkins-pipeline.git'
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
                sh 'docker build -t nusrettinel/java-app .'
            }
        }

        stage('Push Docker Image') {
            steps {
                withDockerRegistry([credentialsId: 'docker-hub-credentials', url: '']) {
                    sh 'docker push nusrettinel/java-app'
                }
            }
        }
    }
}


