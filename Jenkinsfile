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
    				dir("pre-test") {
    					git credentialsId: '7178dfda-0910-40cf-9c42-e7ad0940d5bc', url: 'https://github.com/markusdlugi/test.git', branch: 'pre-test'
    				}
					dir("test") {
    					git credentialsId: '7178dfda-0910-40cf-9c42-e7ad0940d5bc', url: 'https://github.com/markusdlugi/test.git', branch: 'test'
    				}
    			}
    		}
    	}
        stage ("Pre-Test") {
            steps {
				script {
					sh "cp -Rf ./pre-test/* ."
					timeout(time: 60, unit: 'SECONDS') {
						withMaven(maven: 'Maven', mavenSettingsConfig: "e13f1bd0-0910-4c30-b54b-dd526e63e5d6") {
							sh "mvn clean verify"
						}
					}
				}
            }
        }
        stage ("Test") {
            steps {
				script {
					sh "cp -Rf ./test/* ."
					timeout(time: 300, unit: 'SECONDS') {
						withMaven(maven: 'Maven', mavenSettingsConfig: "e13f1bd0-0910-4c30-b54b-dd526e63e5d6") {
							sh "mvn clean verify"
						}
					}
				}
            }
        }
    }
}