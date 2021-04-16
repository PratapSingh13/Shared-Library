#!/usr/bin env groovy
def executeOperation(Map stepParams) 
{
    dir("${stepParams.codePath}") 
    {
        sh "terraform ${stepParams.operation}"
    }
}
