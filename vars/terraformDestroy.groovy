#!/usr/bin env groovy
def executeDestroy(Map stepParams) 
{
  dir("${stepParams.codePath}") 
  {    
    sh "terraform ${stepParams.operation} -lock=false -auto-approve"
  }
}
