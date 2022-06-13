pipeline {
    agent any
    stages {
        stage('Test Java && Scala') {
            agent {
                docker {
                    image 'gradle:7.4.2-jdk11-alpine'
                    reuseNode true // Reuses the current node
                }
            }
            steps {
                sh './gradlew test'
            }
        }

        stage('Test Node') {
            agent {
                docker {
                    image 'node:16-alpine'
                    reuseNode true // Reuses the current node
                }
            }
            steps {
                sh 'npm install'
                sh 'npm run test'
            }
        }
    }
}
