#!/usr/bin/env groovy

def approvalStep() {
    stage("Waiting for Approval") {
        input 'Do you want to proceed or not?'
    }
}

def readPropertyFile(Map stepParams) {
    config = readProperties file: "${stepParams.configFilePath}"
    return config
}
