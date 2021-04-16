#!/usr/bin/env groovy

def checkoutCode() {
    stage("Checking out code repository") 
    {
        checkout scm
    }
}
