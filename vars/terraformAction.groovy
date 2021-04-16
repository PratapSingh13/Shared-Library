#!/usr/bin env groovy
def executeAction(Map stepParams) 
{
    dir("${stepParams.codePath}") 
    {
        
        if ("${stepParams.operation}" == "plan")
        {
            sh "terraform ${stepParams.operation} > plan.out"
        }
        else
        {
            sh "terraform ${stepParams.operation}"
        }
    }
}

def executeLinting(Map stepParams)
{
    dir("${stepParams.codePath}")
    {
        sh "tflint"
    }
}
