#!/usr/bin/env groovy

def checkoutCode() {
    stage("Checking out code repository") 
    {
        checkout scm
    }
}

def checkoutBranch(BRANCH_NAME) {
    stage("Checking out to branch") {
        sh "git checkout ${BRANCH_NAME}"
    }
}
