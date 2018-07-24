pipeline {
    agent {
        docker {
            image 'ntk148v/gradle-git-4.5.1:alpine'
            args '-v $HOME/.m2:/home/gradle/.m2'
        }
    }
    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/ntk148v/opencps-v2.git'
                checkout scm
            }
        }
        stage('Clean') {
            steps {
                sh './gradlew -v'
                // Workaround with 'Gradle locks the global script cache' issue
                sh 'find /home/gradle/.gradle -type f -name "*.lock" | while read f; do rm $f; done'
                sh './gradlew clean --profile'
            }
        }
        stage('Build') {
            steps {
                sh './gradlew buildService --profile'
            }
        }
        stage('Test') {
            steps {
                sh './gradlew test --profile'
            }
            post {
                always {
                    // junit 'modules/backend-api-rest/build/reports/tests/test/index.html'
                    publishHTML (target: [
                        allowMissing: false,
                        alwaysLinkToLastBuild: false,
                        keepAll: true,
                        reportDir: 'modules/backend-api-rest/build/reports/tests/test',
                        reportFiles: 'index.html',
                        reportName: "HTML Report"
                    ])
                }
            }
        }
        stage('SonarQube analysis') {
            steps {
                withSonarQubeEnv('Sonar OpenCPS') {
                    // requires SonarQube Scanner for Gradle 2.1+
                    // It's important to add --info because of SONARJNKNS-281
                    sh './gradlew --info sonarqube'
                }

                script {
                    timeout(time: 1, unit: 'HOURS') { // Just in case something goes wrong, pipeline will be killed after a timeout
                        def qg = waitForQualityGate() // Reuse taskId previously collected by withSonarQubeEnv
                        if (qg.status != 'OK') {
                            error "Pipeline aborted due to quality gate failure: ${qg.status}"
                        }
                    }
                }
            }
        }
    }
    post {
        always {
            echo "Result: ${currentBuild.result}"
        }
    }
}
