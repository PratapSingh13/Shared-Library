def sendEmailNotification(Map stepParams) {
    emailext body: "${stepParams.environment}\n BUILD_ID:- ${env.BUILD_ID}\n JOB_NAME:- ${env.JOB_NAME}\n BUILD_URL:- ${env.BUILD_URL}|BUILD_URL>\n Message:- ${stepParams.message}",
    recipientProviders: [developers()],
    subject: 'satus', to: "${stepParams.mail_id}"
}
