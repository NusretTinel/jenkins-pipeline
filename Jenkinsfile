pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "nusrettinel/java-app"
        DOCKER_CREDENTIALS_ID = "docker-hub-credentials"
        SECOND_SERVER = "nusrettinel2@192.168.1.13"  // Buraya ikinci sunucunun IP'sini koy
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
        sh """
    ssh -i /var/lib/jenkins/.ssh/jenkins-deploy-key -o StrictHostKeyChecking=no nusrettinel2@192.168.1.13 "bash -s" << "EOF"
    docker pull nusrettinel/java-app
    docker ps -q --filter "name=java_app" | grep -q . && docker stop java_app || echo "Container already stopped."
    docker ps -aq --filter "name=java_app" | grep -q . && docker rm java_app || echo "No existing container to remove."
    docker run -d --name java_app -p 8080:8080 nusrettinel/java-app
EOF


        """
    }
}

    }
}
