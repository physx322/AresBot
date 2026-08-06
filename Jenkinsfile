pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                sh './gradlew build'
            }
        }

        stage('Build image') {
            steps {
                sh 'docker build -t ares-bot:${BRANCH_NAME} .'
            }
        }

        stage('Deploy Preprod') {
            when { branch 'develop' }
            environment {
                DISCORD_TOKEN = credentials('discord-token-preprod')
            }
            steps {
                sh '''
                    docker stop bot-preprod || true
                    docker rm bot-preprod || true
                    docker run -d --name bot-preprod \
                        -e BOT_TOKEN=$DISCORD_TOKEN \
                        ares-bot:develop
                '''
            }
        }

        stage('Deploy Prod') {
            when { branch 'master' }
            environment {
                DISCORD_TOKEN = credentials('discord-token-prod')
            }
            steps {
                sh '''
                    docker stop bot-prod || true
                    docker rm bot-prod || true
                    docker run -d --name bot-prod \
                        -e BOT_TOKEN=$DISCORD_TOKEN \
                        ares-bot:master
                '''
            }
        }
    }
}