pipeline {
    agent any
    
    options {
        buildDiscarder(logRotator(numToKeepStr: '5')) // Keep only the last 5 builds
    }
    
    environment {
        SONAR_HOST_URL = 'http://sonarqube:9000' // SonarQube server URL
        SONAR_TOKEN = 'sqp_b1467b24b33dd569eb9ebccb9d4e24f55680e096' // SonarQube authentication token
    }
    
    stages {
        stage('Checkout') {
            steps {
                git branch: 'master', url: 'https://github.com/ahmedbenameur/hi.git'
                
                sh 'pwd'  // Print current working directory
            }
        }
        
        stage('SonarQube Analysis') {
            steps {
                script {
                    /*
                    // Download and install SonarQube Scanner if not already installed
                    sh '''
                        if ! command -v sonar-scanner &> /dev/null; then
                            echo "SonarQube Scanner not found. Installing..."
                            wget https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-5.0.1.3006-linux.zip
                            unzip -o -q sonar-scanner-cli-5.0.1.3006-linux.zip
                        fi
                    '''
                    
                    // Verify SonarQube Scanner installation
                    sh '''
                        echo "PATH: $PATH"
                        ls -lR sonar-scanner-5.0.1.3006-linux
                        which sonar-scanner || echo "SonarQube Scanner not found in PATH"
                    '''
                    */
                }
                
                // Run SonarQube Scanner with the correct environment
                withEnv(["PATH+SCANNER=${WORKSPACE}/sonar-scanner-5.0.1.3006-linux/bin"]) {
                    sh '''
                        sonar-scanner \
                          -Dsonar.projectKey=testtest \
                          -Dsonar.projectName=testtest \
                          -Dsonar.host.url=${SONAR_HOST_URL} \
                          -Dsonar.login=${SONAR_TOKEN} \
                          -Dsonar.sources=. \
                          -Dsonar.java.binaries=target/classes
                    '''
                }
            }
        }
    }
}
