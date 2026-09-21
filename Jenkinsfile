// Works on both Windows (bat) and Linux/Mac (sh) Jenkins agents
def run(String cmd) {
    if (isUnix()) {
        sh cmd
    } else {
        bat cmd
    }
}

pipeline {
    agent any

    // Names must match Manage Jenkins > Tools
    tools {
        maven 'Maven-3.9'
        jdk   'JDK-17'
    }

    options {
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                script { run('mvn -B clean compile') }
            }
        }

        stage('Test') {
            steps {
                script { run('mvn -B test') }
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                script { run('mvn -B package -DskipTests') }
            }
        }

        stage('Archive') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }

    post {
        success { echo 'Build succeeded.' }
        failure { echo 'Build failed - check the console output.' }
    }
}