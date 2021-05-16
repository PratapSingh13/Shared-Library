#!/usr/bin/env groovy
def sendGoogleNotification(Map stepParams) {
  wrap([$class: 'BuildUser']) {
    def user = env.BUILD_USER_ID
    def build_num = env.BUILD_NUMBER
    def job_name = env.JOB_NAME
    googlechatnotification url: "https://chat.googleapis.com/v1/spaces/AAAAuvH0DTI/messages?key=AIzaSyDdI0hCZtE6vySjMm-WEfRq3CPzqKqqsHI&token=6bxkYEGZWPNi1G449fc7FCvyX7PP5v6Xx1rLo3uWLTE%3D",
    notifySuccess: 'false',
    message: "*Job:* ${env.JOB_NAME} \n*Started by:* User *_${user}_* \n*Build Number:* ${env.BUILD_NUMBER} \n*Status:* _${stepParams.buildStatus}_ \n*BUILD_URL:* ${env.BUILD_URL}"   
  }
}
