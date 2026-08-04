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
            script {
                def duration = currentBuild.durationString.replace(' and counting', '')
                discordSend(
                    webhookURL: "${DISCORD_URL}",
                    title: "✅ Build réussi — ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                    description: """
                        **Projet:** ${env.JOB_NAME}
                        **Branche:** ${env.GIT_BRANCH}
                        **Commit:** ${env.GIT_COMMIT?.take(7)}
                        **Auteur:** ${env.GIT_AUTHOR_NAME ?: 'N/A'}
                        **Durée:** ${duration}
                    """,
                    link: env.BUILD_URL,
                    successful: true,
                    footer: "Jenkins Pipeline"
                )
            }
        }
    }
    failure {
        withCredentials([string(credentialsId: 'discord-webhook', variable: 'DISCORD_URL')]) {
            script {
                def duration = currentBuild.durationString.replace(' and counting', '')
                discordSend(
                    webhookURL: "${DISCORD_URL}",
                    title: "❌ Build échoué — ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                    description: """
                        **Projet:** ${env.JOB_NAME}
                        **Branche:** ${env.GIT_BRANCH}
                        **Commit:** ${env.GIT_COMMIT?.take(7)}
                        **Desctiption** ${env.GIT_COMMIT?.}
                        **Auteur:** ${env.GIT_AUTHOR_NAME ?: 'N/A'}
                        **Durée:** ${duration}
                        **Étape échouée:** ${env.STAGE_NAME ?: 'Inconnue'}
                    """,
                    link: env.BUILD_URL,
                    result: currentBuild.currentResult,
                    footer: "Jenkins Pipeline"
                )
            }
        }
    }
}
}
