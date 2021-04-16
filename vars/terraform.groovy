#!/usr/bin/env groovy

def codeCheckout(Map stepParams)
{
    stage("Code Checkout")
    {
        //git branch: "${stepParams.branch}",
        //url: "${stepParams.git_url}"
        echo "${stepParams.git_url}"
        git branch: 'main', url: "${stepParams.git_url}"
        //git "${stepParams.git_url}"
        
        
        //try
        //{
        //    git '${git_url}'
        //}
        //catch(Exception e)
        //{
          //  echo "Failed while Code Checkout"
            //sendFailedNotification(
            //channelName: "${stepParams.slackChannel}",
            //message: "Failed while Code Checkout"
        //)
        //echo e.toString()
        //throw e
        //}
    }
}

def terraformAction(Map stepParams)
{
    dir("${stepParams.codePath}")
    {
        sh "terraform ${stepParams.operation}"
    }
}

def initializeTerraform(Map stepParams)
{
    stage("Initializing Terraform")
    {
        try
        {
            terraformAction(
                operation: "init"
            )
        }
        catch(Exception e)
        {
            echo "Failed while initializing terraform modules"
            //sendFailedNotification(
            //channelName: "${stepParams.slackChannel}",
            //message: "Failed while initializing terraform modules"
        //)
        echo e.toString()
        throw e
        }
    }
}

def lintTerraformCode(Map stepParams)
{
    stage("Linting Terraform Code")
    {
        try
        {
            terraformAction(
                operation: "fmt -list=true -write=false -diff=true"
            )
        }
        catch(Exception e)
        {
            echo "Failed while linting code"
            //sendFailedNotification(
            //channelName: "${stepParams.slackChannel}",
            //message: "Failed while linting code"
        //)
        echo e.toString()
        throw e
        }
    }
}

def validateTerraform(Map stepParams)
{
    stage("Validating Terraform Code")
    {
        try
        {
            terraformAction(
                operation: "plan"
            )
        }
        catch(Exception e)
        {
            echo "Failed while validating"
            //sendFailedNotification(
            //channelName: "${stepParams.slackChannel}",
            //message: "Failed while Validating"
        //)
        echo e.toString()
        throw e
        }
    }
}

def planInfrastructure(Map stepParams)
{
    stage("Terraform Plan")
    {
        try
        {
            terraformAction(
                operation: "plan"
            )
        }
        catch(Exception e)
        {
            echo "Failed while planning infrastructure"
            //sendFailedNotification(
            //channelName: "${stepParams.slackChannel}",
            //message: "Failed while planning"
        //)
        echo e.toString()
        throw e
        }
    }
}

def createInfrastructure(Map stepParams)
{
    stage("Applying Terraform")
    {
        try
        {
            terraformAction(
                operation: "apply -lock=false -auto-approve"
            )
        }
        catch(Exception e)
        {
            echo "Failed while creating infrastructure"
            //sendFailedNotification(
            //channelName: "${stepParams.slackChannel}",
            //message: "Failed while applying"
        //)
        echo e.toString()
        throw e
        }
    }
}
