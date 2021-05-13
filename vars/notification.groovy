#!/usr/bin/env groovy
def sendSlackNotification(Map stepParams) {
  slackSend channel: "${stepParams.slackChannel}",
  color: "${stepParams.buildStatus}",
  message: "JOB_NAME:- ${env.JOB_NAME}\n BUILD_URL:- ${env.BUILD_URL}\n"
}
