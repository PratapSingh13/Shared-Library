#!/usr/bin env groovy
def executeAction(Map stepParams) 
{
  dir("${stepParams.codePath}") 
  {  
    if ("${stepParams.operation}" == "apply")
    {
      sh "terraform ${stepParams.operation} -lock=false -auto-approve"
    }
    else
    {
      sh "terraform ${stepParams.operation}"
    }
    if ("${stepParams.operation}" == "destroy")
    {
      sh "terraform ${stepParams.operation} -lock=false -auto-approve"
    }
  }
}
