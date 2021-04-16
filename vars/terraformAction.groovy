#!/usr/bin env groovy
def executeAction(Map stepParams) 
{
    dir("${stepParams.codePath}") 
    {
        sh "terraform ${stepParams.operation}"
    }
}
