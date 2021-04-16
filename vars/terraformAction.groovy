#!/usr/bin env groovy
def executeActions(Map stepParams) 
{
    dir("${stepParams.codePath}") 
    {
        sh "terraform ${stepParams.operation}"
    }
}
