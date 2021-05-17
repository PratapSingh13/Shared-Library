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
  stage("Sending success notification on Google Chat") 
  {
    googleChatNotification.sendGoogleNotification(
      buildStatus: "BUILD SUCCESSFUL",
      message: "${stepParams.message}"
    )
  }
}
def sendFailNotification(Map stepParams) 
{
  stage("Sending failure notification on Google Chat") 
  {
    googleChatNotification.sendGoogleNotification(
      buildStatus: "BUILD FAILED",
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
    sendFailNotification(
      message: "*Job:* ${env.JOB_NAME} \n*Started by:* User *_${env.BUILD_USER_ID}_* \n*Build Number:* ${env.BUILD_NUMBER} \n*Status:* _BUILD FAILED_ \n*Message:* Unable to clone CodeBase \n*BUILD_URL:* ${env.BUILD_URL}"
    )
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
      message: "*Job:* ${env.JOB_NAME} \n*Started by:* User *_${env.BUILD_USER_ID}_* \n*Build Number:* ${env.BUILD_NUMBER} \n*Status:* _BUILD FAILED_ \n*Message:* Unable to initialize terraform \n*BUILD_URL:* ${env.BUILD_URL}"
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
      message: "*Job:* ${env.JOB_NAME} \n*Started by:* User *_${env.BUILD_USER_ID}_* \n*Build Number:* ${env.BUILD_NUMBER} \n*Status:* _BUILD FAILED_ \n*Message:* Failed while applying formatting into terraform code! Please look into your code \n*BUILD_URL:* ${env.BUILD_URL}"
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
      message: "*Job:* ${env.JOB_NAME} \n*Started by:* User *_${env.BUILD_USER_ID}_* \n*Build Number:* ${env.BUILD_NUMBER} \n*Status:* _BUILD FAILED_ \n*Message:* Failed while terraform code validation! Please look into your code \n*BUILD_URL:* ${env.BUILD_URL}"
    )
    echo e.toString()
    throw e
  }
  try 
  {
    lintingTerraformCode(
      codeBasePath: "${config.CODE_BASE_PATH}"
    )
  } 
  catch (Exception e) 
  {
    echo "Failed while linting Terraform Code! Please look into your code"
    sendFailNotification(
      message: "*Job:* ${env.JOB_NAME} \n*Started by:* User *_${env.BUILD_USER_ID}_* \n*Build Number:* ${env.BUILD_NUMBER} \n*Status:* _BUILD FAILED_ \n*Message:* Failed while linting terraform code! Please look into your code \n*BUILD_URL:* ${env.BUILD_URL}"
    )
    echo e.toString()
    throw e
  }
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
      message: "*Job:* ${env.JOB_NAME} \n*Started by:* User *_${env.BUILD_USER_ID}_* \n*Build Number:* ${env.BUILD_NUMBER} \n*Status:* _BUILD FAILED_ \n*Message:* Failed while terraform planning \n*BUILD_URL:* ${env.BUILD_URL}"
    )
    echo e.toString()
    throw e
  }
  //if("${env.BRANCH_NAME}" == "master") 
  //{
    if("${config.KEEP_APPROVAL_STAGE}" == "true" || "${config.KEEP_APPROVAL_STAGE}" == "null")
    {
      commonfile.approvalStep()
    }
    try
    {
      createInfrastructure(
        codeBasePath: "${config.CODE_BASE_PATH}"
      )
    }
    catch (Exception e) 
    {
      echo "Failed while creating Infrastructure"
      sendFailNotification(
        message: "*Job:* ${env.JOB_NAME} \n*Started by:* User *_${env.BUILD_USER_ID}_* \n*Build Number:* ${env.BUILD_NUMBER} \n*Status:* _BUILD FAILED_ \n*Message:* Failed while creating infrastructure \n*BUILD_URL:* ${env.BUILD_URL}"
      )
      echo e.toString()
      throw e
    }
    sendSuccessNotification(
      message: "*Job:* ${env.JOB_NAME} \n*Started by:* User *_${env.BUILD_USER_ID}_* \n*Build Number:* ${env.BUILD_NUMBER} \n*Status:* _BUILD SUCCESSFULLY_ \n*Message:* Terraform build successfully applied \n*BUILD_URL:* ${env.BUILD_URL}" 
    )
  //}
  // else
  // {
  //   echo "Skipping execution because of non-master branch"
  //   sendFailNotification(
  //     message: "*Job:* ${env.JOB_NAME} \n*Started by:* User *_${env.BUILD_USER_ID}_* \n*Build Number:* ${env.BUILD_NUMBER} \n*Status:* _BUILD SUCCESSFULLY_ \n*Message:* Failed to create Infrastructure due to non-master branch \n*BUILD_URL:* ${env.BUILD_URL}"
  //   )
  // }
}
