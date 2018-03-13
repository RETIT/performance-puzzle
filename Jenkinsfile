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
    					git credentialsId: 'github', url: 'https://github.com/markusdlugi/test.git', branch: 'pre-test'
    				}
					dir("/home/builduser/test") {
    					git credentialsId: 'github', url: 'https://github.com/markusdlugi/test.git', branch: 'test'
    				}
    			}
    		}
    	}
        stage ("Pre-Test") {
            steps {
				script {
					sh "cp -Rf /home/builduser/pre-test/* ."
					timeout(time: 120, unit: 'SECONDS') {
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
					sh "cp -Rf /home/builduser/test/* ."
					timeout(time: 360, unit: 'SECONDS') {
						withMaven(maven: 'Maven', mavenSettingsConfig: "e13f1bd0-0910-4c30-b54b-dd526e63e5d6") {
							sh "mvn clean verify"
						}
					}
				}
            }
        }
    }
}