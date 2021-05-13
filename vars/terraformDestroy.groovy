#!/usr/bin env groovy
def destroyInfrastructure(Map stepParams) 
{
  stage("Destroying Terraform") 
  {
    terraformAction.executeAction(
      codePath: "${config.CODE_BASE_PATH}",
      operation: "destroy -lock=false -auto-approve"
    )
  }
}

def sendSuccessNotification(Map stepParams) 
{
  stage("Sending success notification on slack") 
  {
    notification.sendSlackNotification(
      slackChannel: "${stepParams.channelName}",
      buildStatus: "good",
      message: "${stepParams.message}"
    )
  }
}

def sendFailNotification(Map stepParams) 
{
  stage("Sending failure notification on slack") 
  {
    notification.sendSlackNotification(
      slackChannel: "${stepParams.channelName}",
      buildStatus: "danger",
      message: "${stepParams.message}"
    )
  }
}

def call(Map stepParams) {
  try 
  {
    config = commonfile.readPropertyFile(
      configFilePath: "${stepParams.configFilePath}"
    )
  }
  catch (Exception e) 
  {
    echo "Sorry I'm unable to read Config file"
    echo e.toString()
    throw e
  }  
    
  try 
  {
  input message: 'Press Yes to apply changes', ok: 'YES'
  destroyInfrastructure(
    codeBasePath: "${config.CODE_BASE_PATH}"
    )
  } 
  catch (Exception e) 
  {
    echo "Unable to Destroy Terraform"
    sendFailNotification(
      channelName: "${config.SLACK_CHANNEL_NAME}",
      message: "Failed while destroying"
    )
    echo e.toString()
    throw e
  }
  sendSuccessNotification(
    channelName: "${config.SLACK_CHANNEL_NAME}",
    message: "Successfully Destroyed"   
  )
}



def sendSlackNotification(Map stepParams) {
  slackSend channel: "${stepParams.slackChannel}",
  color: "${stepParams.buildStatus}",
  message: "JOB_NAME:- ${env.JOB_NAME}\n BUILD_URL:- ${env.BUILD_URL}\n"
}
