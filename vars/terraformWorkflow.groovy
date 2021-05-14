#!/usr/bin/env groovy
def initializeTerraform(Map stepParams) 
{
  stage("Terraform Initializing") 
  {
    terraformAction.executeAction(
      codePath: "${config.CODE_BASE_PATH}",
      operation: "init"
    )
  }
}
def formattingTerraformCode(Map stepParams) 
{
  stage("Formatting Terraform Code") 
  {
    terraformAction.executeAction(
      codePath: "${config.CODE_BASE_PATH}",
      operation: "fmt -list=true -write=false -diff=true"
    )
  }
}
def validateTerraformCode(Map stepParams) 
{
  stage("Validating Terraform Code") 
  {
    terraformAction.executeAction(
      codePath: "${config.CODE_BASE_PATH}",
      operation: "validate"
    )
  }
}
def planInfrastructure(Map stepParams) 
{
  stage("Planning Terraform Code") 
  {
    terraformAction.executeAction(
      codePath: "${config.CODE_BASE_PATH}",
      operation: "plan"
    )
  }
}
def lintingTerraformCode(Map stepParams) 
{
  stage("Linting Terraform Code") 
  {
    terraformAction.executeLinting(
      codePath: "${config.CODE_BASE_PATH}"
    )
  }
}
def createInfrastructure(Map stepParams) 
{
  stage("Applying Terraform") 
  {
    terraformAction.executeAction(
      codePath: "${config.CODE_BASE_PATH}",
      operation: "apply -lock=false -auto-approve"
    )
  }
}
def sendSuccessNotification(Map stepParams) 
{
  stage("Sending success notification on slack") 
  {
    notification.sendGoogleNotification(
      buildStatus: "good",
      message: "${stepParams.message}"
    )
  }
}
def sendFailNotification(Map stepParams) 
{
  stage("Sending failure notification on slack") 
  {
    notification.sendGoogleNotification(
      buildStatus: "danger",
      message: "${stepParams.message}"
    )
  }
}
def call(Map stepParams) {
  try 
  {
    git.checkoutCode()
  } 
  catch (Exception e) 
  {
    echo "Unable to clone CodeBase"
    echo e.toString()
    throw e
  }
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
    initializeTerraform(
      codeBasePath: "${config.CODE_BASE_PATH}",
    )
  } 
  catch (Exception e) 
  {
    echo "Unable to initialize Terraform"
    sendFailNotification(
      message: "Unable to initialize Terraform"
    )
    echo e.toString()
    throw e
  }
  try 
  {
    formattingTerraformCode(
      codeBasePath: "${config.CODE_BASE_PATH}"
    )
  } 
  catch (Exception e) 
  {
    echo "Failed while formatting Terraform Code! Please look into your code"
    sendFailNotification(
      message: "Failed while linting Terraform Code! Please look into your code"
    )
    echo e.toString()
    throw e
  }
  try 
  {
    validateTerraformCode(
      codeBasePath: "${config.CODE_BASE_PATH}"
    )
  } 
  catch (Exception e) 
  {
    echo "Failed while Terraform Code Validation! Please look into your code"
    sendFailNotification(
      message: "Failed while Terraform Code Validation! Please look into your code"
    )
    echo e.toString()
    throw e
  }
  // try 
  // {
  //   lintingTerraformCode(
  //     codeBasePath: "${config.CODE_BASE_PATH}"
  //   )
  // } 
  // catch (Exception e) 
  // {
  //   echo "Failed while linting Terraform Code! Please look into your code"
  //   sendFailNotification(
  //     channelName: "${config.SLACK_CHANNEL_NAME}",
  //     message: "Failed while linting Terraform Code! Please look into your code"
  //   )
  //   echo e.toString()
  //   throw e
  // }
  try 
  {
    planInfrastructure(
      codeBasePath: "${config.CODE_BASE_PATH}",
    )
  } 
  catch (Exception e) 
  {
    echo "Failed during planning Infrastructure"
    sendFailNotification(
      message: "Failed while planning"
    )
    echo e.toString()
    throw e
  }
  try 
  {
    //input message: 'Press Yes to apply changes', ok: 'YES'
    createInfrastructure(
      codeBasePath: "${config.CODE_BASE_PATH}"
    )
  } 
  catch (Exception e) 
  {
    echo "Unable to Apply Terraform"
    sendFailNotification(
      message: "Failed while applying"
    )
    echo e.toString()
    throw e
  }
  sendSuccessNotification(
    message: "Successfully applied"   
  )
}

// def sendGoogleNotification(Map stepParams) {
//   // if ( buildResult == "SUCCESS" ) {
//   googlechatnotification url: "https://chat.googleapis.com/v1/spaces/AAAAuvH0DTI/messages?key=AIzaSyDdI0hCZtE6vySjMm-WEfRq3CPzqKqqsHI&token=6bxkYEGZWPNi1G449fc7FCvyX7PP5v6Xx1rLo3uWLTE%3D",
//   message: "*Job* :  *${env.JOB_NAME}* started by *${env.BUILD_USER_ID}* user with buildnumber *${env.BUILD_NUMBER}* was ${stepParams.buildStatus} . BUILD_URL:- ${env.BUILD_URL}"   
//   // }
// }










