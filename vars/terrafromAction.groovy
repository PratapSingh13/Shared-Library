#!/usr/bin/env groovy
def call(Map stepsParams)
{
    stage('Code Checkout')
    {
        git branch: "${stepsParams.branch}",
        url: "${stepsParams.git_url}"
    }
    stage('Initializing Terraform Code')
    {
        sh 'terraform init'
        echo "Hello Y"
    }
    stage('Validating Terraform Code')
    {
        sh 'terraform validate'
        echo "Hello YP"
    }
    stage('Checking Formatting')
    {
        //sh 'terraform fmt'
        echo "Hello YPS"
    }
    stage('Applying Plan')
    {
        //sh 'terraform plan'
        echo "Hello YPSB"
    }
    stage('Applying Terraform Code')
    {
        //input message: 'Press Yes to apply changes', ok: 'YES'
        //h 'terraform apply -lock=false -auto-approve'
        echo "Hello P"
    }
}
