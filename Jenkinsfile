pipeline {
    agent any
    stages {
        stage('Kod Çekme') {
            steps {
                git url: 'https://github.com/NusretTinel/jenkins-pipeline.git', branch: 'main'
            }
        }
        stage('Derleme') {
            steps {
                sh 'javac Main.java'
                sh 'jar cvf app.jar *.class'
            }
        }
        stage('Docker İmajı Oluşturma') {
            steps {
                script {
                    def appImage = docker.build("nusrettinel/app:${env.BUILD_NUMBER}")
                    docker.withRegistry('https://index.docker.io/v1/', 'dockerhub-credentials') {
                        appImage.push()
                    }
                }
            }
        }
        stage('Dağıtım') {
    steps {
        sshagent(['app-server-ssh']) {
            sh 'ssh jenkins-admin@192.168.1.16 docker stop app || true'
            sh 'ssh jenkins-admin@192.168.1.16 docker rm app || true'
            sh 'bash -c "ssh jenkins-admin@192.168.1.16 docker run -d --name app -p 8081:8080 nusrettinel/app:${env.BUILD_NUMBER}"'
        }
    }
}
    }
}
