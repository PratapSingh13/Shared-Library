#!/usr/bin/env groovy
def sendGoogleNotification(Map stepParams) {
  wrap([$class: 'BuildUser']) {
    def user = env.BUILD_USER_ID
    def build_num = env.BUILD_NUMBER
    def job_name = env.JOB_NAME
    googlechatnotification url: "${config.GOOGLE_CHAT_URL}",
    notifySuccess: 'false',
    message: "*Job:* ${env.JOB_NAME} \n*Started by:* User *_${user}_* \n*Build Number:* ${env.BUILD_NUMBER} \n*Status:* _${stepParams.buildStatus}_ \n*BUILD_URL:* ${env.BUILD_URL}"   
  }
}