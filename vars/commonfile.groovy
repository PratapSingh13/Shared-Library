#!/usr/bin/env groovy

def readPropertyFile(Map stepParams) {
    config = readProperties file: "${stepParams.configFilePath}"
    return config
}
