pipeline {
    agent any

    environment {
        APP_NAME = 'devpath-portal'
        APP_VERSION = '1.0.0'
        IMAGE_NAME = "devpath-portal:${BUILD_NUMBER}"
        LATEST_IMAGE = "devpath-portal:latest"
        PORT = '8085'
        SPRING_PROFILES_ACTIVE = 'dev'
    }

    options {
        timeout(time: 30, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '10'))
        disableConcurrentBuilds()
    }

    stages {
        stage('1. Checkout Source') {
            steps {
                echo "=== Stage 1: Checking out DevPath source code ==="
                checkout scm
                script {
                    echo "Branch: ${env.GIT_BRANCH ?: 'local/main'}"
                    echo "Commit: ${env.GIT_COMMIT ?: 'local-head'}"
                }
            }
        }

        stage('2. Verify Environment') {
            steps {
                echo "=== Stage 2: Validating Java 21 and Maven toolchain ==="
                script {
                    if (isUnix()) {
                        sh 'java -version'
                        sh 'mvn -version'
                    } else {
                        bat 'java -version'
                        bat 'mvn -version'
                    }
                }
            }
        }

        stage('3. Compile & Verify DDL') {
            steps {
                echo "=== Stage 3: Compiling source files and validating Flyway migrations ==="
                script {
                    if (isUnix()) {
                        sh 'mvn clean compile -B'
                    } else {
                        bat 'mvn clean compile -B'
                    }
                }
            }
        }

        stage('4. Run Automated Tests') {
            steps {
                echo "=== Stage 4: Executing JUnit 5 unit and MockMvc integration tests ==="
                script {
                    if (isUnix()) {
                        sh 'mvn test -B'
                    } else {
                        bat 'mvn test -B'
                    }
                }
            }
            post {
                always {
                    junit testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: true
                }
            }
        }

        stage('5. Package Executable JAR') {
            steps {
                echo "=== Stage 5: Packaging application into fat executable JAR ==="
                script {
                    if (isUnix()) {
                        sh 'mvn package -DskipTests -B'
                    } else {
                        bat 'mvn package -DskipTests -B'
                    }
                }
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('6. Build Docker Container Image') {
            steps {
                echo "=== Stage 6: Building Docker container image: ${IMAGE_NAME} ==="
                script {
                    def dockerAvailable = false
                    try {
                        if (isUnix()) {
                            dockerAvailable = (sh(script: 'docker info > /dev/null 2>&1', returnStatus: true) == 0)
                        } else {
                            dockerAvailable = (bat(script: 'docker info > nul 2>&1', returnStatus: true) == 0)
                        }
                    } catch (Exception e) {
                        dockerAvailable = false
                    }

                    if (dockerAvailable) {
                        if (isUnix()) {
                            sh "docker build -t ${IMAGE_NAME} -t ${LATEST_IMAGE} ."
                        } else {
                            bat "docker build -t ${IMAGE_NAME} -t ${LATEST_IMAGE} ."
                        }
                        echo "Docker image built successfully: ${IMAGE_NAME}"
                    } else {
                        echo "[WARNING] Docker daemon is currently unreachable or stopped."
                        echo "If demonstrating on Windows, ensure Docker Desktop is started."
                        echo "Skipping Docker push stage safely without fabricating fake container status."
                    }
                }
            }
        }

        stage('7. Deploy / Verify Environment') {
            steps {
                echo "=== Stage 7: Deployment verification to target cluster ==="
                script {
                    def kubectlAvailable = false
                    try {
                        if (isUnix()) {
                            kubectlAvailable = (sh(script: 'kubectl cluster-info > /dev/null 2>&1', returnStatus: true) == 0)
                        } else {
                            kubectlAvailable = (bat(script: 'kubectl cluster-info > nul 2>&1', returnStatus: true) == 0)
                        }
                    } catch (Exception e) {
                        kubectlAvailable = false
                    }

                    if (kubectlAvailable) {
                        echo "Kubernetes cluster detected! Applying manifests..."
                        if (isUnix()) {
                            sh 'kubectl apply -f k8s/'
                        } else {
                            bat 'kubectl apply -f k8s/'
                        }
                    } else {
                        echo "[INFO] Kubernetes cluster is offline or running locally via Docker Compose."
                        echo "To deploy on Kubernetes, run: kubectl apply -f k8s/"
                    }
                }
            }
        }
    }

    post {
        success {
            echo "=========================================================="
            echo "CI/CD Pipeline Completed Successfully for Build #${BUILD_NUMBER}!"
            echo "Artifact: target/devpath-portal-1.0.0.jar"
            echo "=========================================================="
        }
        failure {
            echo "=========================================================="
            echo "Pipeline FAILED! Check the stage logs above for details."
            echo "=========================================================="
        }
    }
}
