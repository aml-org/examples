def lastStage = ""
def color = '#FF8C00'
def headerFlavour = "WARNING"
def SLACK_CHANNEL = '#amf-jenkins'
def PRODUCT_NAME = "AMF-EXAMPLES"

pipeline {
  agent any
  options {
    timeout(time: 30, unit: 'MINUTES')
    ansiColor('xterm')
  }
  stages {
    stage('Test Java && Scala') {
      agent {
        docker {
          image 'gradle:7.4.2-jdk11-alpine'
          registryCredentialsId 'dockerhub-pro-credentials'
          reuseNode true // Reuses the current node
        }
      }
      steps {
        script {
          lastStage = env.STAGE_NAME
          sh './gradlew test'
        }
      }
    }

    stage('Test Node') {
      agent {
        docker {
          image 'node:16-alpine'
          registryCredentialsId 'dockerhub-pro-credentials'
          reuseNode true // Reuses the current node
        }
      }
      steps {
        script {
          lastStage = env.STAGE_NAME
          sh 'cd AMF5 && npm install && npm run test'
        }
      }
    }
  }

  post {
    unsuccessful {
      script {
        if (isMaster()) {
          sendBuildErrorSlackMessage(lastStage, SLACK_CHANNEL, PRODUCT_NAME)
        } else {
          echo "Unsuccessful build: skipping slack message notification as branch is not master or develop"
        }
      }
    }
    success {
      script {
        echo "SUCCESSFUL BUILD"
        if (lastBuildFailed() && isMaster()) {
          slackSend color: '#00FF00', channel: SLACK_CHANNEL, message: ":ok_hand: examples: Everything back to normal :ok_hand:"
        }
      }
    }
  }
}

Boolean isMaster() {
  env.BRANCH_NAME == "master"
}

Boolean lastBuildFailed() {
    def lastBuild = currentBuild.getPreviousBuild()
    lastBuild != null && lastBuild.result == "FAILURE"
}

def sendBuildErrorSlackMessage(String lastStage, String slackChannel, String productName) {
  def color = '#FF8C00'
  def headerFlavour = 'WARNING'
  if (isMaster()) {
    color = '#FF0000'
    headerFlavour = "RED ALERT"
  } else if (isDevelop()) {
    color = '#FFD700'
  }
  def message = """:alert: ${headerFlavour}! :alert: Build failed!.
                  |Branch: ${env.BRANCH_NAME}
                  |Stage: ${lastStage}
                  |Product: ${productName}
                  |Build URL: ${env.BUILD_URL}""".stripMargin().stripIndent()
  slackSend color: color, channel: "${slackChannel}", message: message
}

def sendSuccessfulSlackMessage(String slackChannel, String productName) {
  slackSend color: '#00FF00', channel: "${slackChannel}", message: ":ok_hand: ${productName} Master Publish OK! :ok_hand:"
}
