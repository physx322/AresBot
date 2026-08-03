pipeline {
    agent any

    tools {
        jdk 'jdk-21'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'master',
                    url: 'https://github.com/physx322/ClicBot.git',
                    credentialsId: 'gh'
            }
        }

        stage('Build') {
            steps {
                sh 'chmod +x gradlew'
                sh './gradlew clean build -x test'
            }
        }

        stage('Archive') {
            steps {
                archiveArtifacts artifacts: 'build/libs/*.jar', fingerprint: true
            }
        }
    }
    post {
        success {
            withCredentials([string(credentialsId: 'discord-webhook', variable: 'DISCORD_URL')]) {
                discordSend(
                    webhookURL: "${DISCORD_URL}",
                    title: "✅ Build réussi — ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                    description: "Le build s'est terminé avec succès.",
                    link: env.BUILD_URL,
                    successful: true
                )
            }
        }
        failure {
            withCredentials([string(credentialsId: 'discord-webhook', variable: 'DISCORD_URL')]) {
                discordSend(
                    webhookURL: "${DISCORD_URL}",
                    title: "❌ Build échoué — ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                    description: "Le build a échoué.",
                    link: env.BUILD_URL,
                    result: currentBuild.currentResult
                )
            }
        }
    }
}
