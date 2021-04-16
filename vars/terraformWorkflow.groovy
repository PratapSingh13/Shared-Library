#!/usr/bin/env groovy
def initializeTerraform(Map stepParams) {
    stage("Initializing Terraform") {
        terraform.executeOperation(
            codePath: "${config.CODE_BASE_PATH}",
            operation: "init"
        )
    }
}

def lintTerraformCode(Map stepParams) {
    stage("Linting Terraform Code") {
        terraform.executeOperation(
            codePath: "${config.CODE_BASE_PATH}",
            operation: "fmt -list=true -write=false -diff=true"
        )
    }
}

def planInfrastructure(Map stepParams) {
    stage("Planning Terraform Code") {
        terraform.executeOperation(
            codePath: "${config.CODE_BASE_PATH}",
            operation: "plan"
        )
    }
}

def createInfrastructure(Map stepParams) {
    stage("Applying Terraform") {
        terraform.executeOperation(
            codePath: "${config.CODE_BASE_PATH}",
            operation: "apply -auto-approve"
        )
    }
}

def sendSuccessNotification(Map stepParams) {
    stage("Sending success notification on slack") {
        notification.sendSlackNotification(
            slackChannel: "${stepParams.channelName}",
            buildStatus: "good",
            message: "${stepParams.message}"
        )
    }
}

def sendFailNotification(Map stepParams) {
    stage("Sending failure notification on slack") {
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
        sh 'pwd'
        git.checkoutCode()
    } 
    catch (Exception e) 
    {
        echo "Failed while clonning the codebase"
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
        echo "Failed while reading config file"
        echo e.toString()
        throw e
    }

    try 
    {
        sh 'pwd'
        lintTerraformCode(
            codeBasePath: "${config.CODE_BASE_PATH}"
        )
    } 
    catch (Exception e) 
    {
        echo "Failed while linting code"
        sendFailNotification(
            channelName: "${config.SLACK_CHANNEL_NAME}",
            message: "Failed while linting code"
        )
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
        echo "Failed while initializing terraform modules"
        sendFailNotification(
            channelName: "${config.SLACK_CHANNEL_NAME}",
            message: "Failed while initializing terraform modules"
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
        echo "Failed while planning infrastructure"
        sendFailNotification(
            channelName: "${config.SLACK_CHANNEL_NAME}",
            message: "Failed while planning"
        )
        echo e.toString()
        throw e
    }

    try 
    {
        createInfrastructure(
            codeBasePath: "${config.CODE_BASE_PATH}"
        )
    } 
    catch (Exception e) 
    {
        echo "Failed while creating infrastructure"
        sendFailNotification(
            channelName: "${config.SLACK_CHANNEL_NAME}",
            message: "Failed while applying"
        )
        echo e.toString()
        throw e
    }
    sendSuccessNotification(
        channelName: "${config.SLACK_CHANNEL_NAME}",
        message: "Successfully applied"   
    )
}
