pipeline {
    agent {
        node { label "puzzle-agent" }
    }
    tools {
        maven "Maven"
        jdk "OpenJDK 1.8.0"
    }
    stages {
        stage ("Checkout Test") {
            steps {
                script {
                    dir("/home/builduser/pre-test") {
                        git credentialsId: 'github', url: 'https://github.com/RETIT/performance-puzzle.git', branch: 'pre-test'
                    }
                    dir("/home/builduser/test") {
                        git credentialsId: 'github', url: 'https://github.com/RETIT/performance-puzzle.git', branch: 'test'
                    }
                }
            }
        }
        stage ("Pre-Test") {
            steps {
                script {
                    sh "cp -Rf /home/builduser/pre-test/* ."
                    timeout(time: 120, unit: 'SECONDS') {
                        sh "mvn clean verify"
                    }
                }
            }
        }
        stage ("Test") {
            steps {
                script {
                    sh "cp -Rf /home/builduser/test/* ."
                    timeout(time: 360, unit: 'SECONDS') {
                        sh "mvn clean verify"
                    }
                }
            }
        }
    }
}