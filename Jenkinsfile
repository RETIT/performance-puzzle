pipeline {
	agent {
		node { label "puzzle-agent" }
	}
    tools {
        maven "Maven"
        jdk "OpenJDK 1.8.0"
    }
    stages {
        stage ("Build") {
            steps {
				script {
					withMaven(maven: 'Maven', mavenSettingsConfig: "e13f1bd0-0910-4c30-b54b-dd526e63e5d6") {
						sh "mvn clean verify"
					}
				}
            }
        }
    }
}