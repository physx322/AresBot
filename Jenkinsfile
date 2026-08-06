pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                sh 'chmod +x ./gradlew'
                sh './gradlew jar'
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
                    docker stop ares-preprod || true
                    docker rm ares-preprod || true
                    docker run -d --name ares-preprod \
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
                    docker stop ares-prod || true
                    docker rm ares-prod || true
                    docker run -d --name bot-prod \
                        -e BOT_TOKEN=$DISCORD_TOKEN \
                        ares-bot:master
                '''
            }
        }
    }
}