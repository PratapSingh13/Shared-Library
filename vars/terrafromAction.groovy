#!/usr/bin/env groovy
def call(String terraformAction)
{
    stage('Code Checkout')
    {
        git branch: "${stepsParams.branch}",
        url: "${stepsParams.git_url}"
    }
    stage('Initializing Terraform Code')
    {
        sh 'terraform init'
    }
    stage('Validating Terraform Code')
    {
        sh 'terraform validate'
    }
    stage('Checking Formatting')
    {
        sh 'terraform fmt'
    }
    stage('Applying Plan')
    {
        sh 'terraform plan'
    }
    stage('Applying Terraform Code')
    {
        input message: 'Press Yes to apply changes', ok: 'YES'
        sh 'terraform apply -lock=false -auto-approve'
    }
}
