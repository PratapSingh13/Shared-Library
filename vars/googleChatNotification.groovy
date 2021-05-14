#!/usr/bin/env groovy
def sendGoogleNotification(Map stepParams) {
  wrap([$class: 'BuildUser']) {
    def user = env.BUILD_USER_ID
    def build_num = env.BUILD_NUMBER
    def job_name = env.JOB_NAME
    googlechatnotification url: "https://chat.googleapis.com/v1/spaces/AAAAuvH0DTI/messages?key=AIzaSyDdI0hCZtE6vySjMm-WEfRq3CPzqKqqsHI&token=6bxkYEGZWPNi1G449fc7FCvyX7PP5v6Xx1rLo3uWLTE%3D",
    message: "*Job* :  *${env.JOB_NAME}* started by *${user}* user with buildnumber *${env.BUILD_NUMBER}* was ${stepParams.buildStatus} . BUILD_URL:- ${env.BUILD_URL}"   
  }
}

sendGoogleChatBuildReport(Version: env.VERSION,
    message: "This is a <strike>simple</strike> <i>card<i> text message " +
                 "with a <a href=\"https://github.com/mkutz/jenkins-google-chat-notification\">link</a>" +
                 "<br>and a line break, " +
                 "which does not support mention @all users in the Group.")
