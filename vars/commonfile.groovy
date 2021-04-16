#!/usr/bin/env groovy

def approvalStep() {
    input message: 'Press Yes to apply changes', ok: 'YES'
}

def readPropertyFile(Map stepParams) {
    config = readProperties file: "${stepParams.configFilePath}"
    return config
}
