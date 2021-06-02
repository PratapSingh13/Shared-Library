#!/usr/bin/env groovy
def buildDeployer(Map stepParams)
{
    try
    {
        git.checkoutCode()
    }
    catch(Exception e)
    {
        echo "Unable to clone the Repository"
        echo e.toString()
        throw e
    }
    try 
    {
        config = commonfile.readPropertyFile(
            configFilePath: "${stepParams.configFilePath}"
        )
    }
    catch (Exception e) 
    {
        echo "Sorry I'm unable to read Config file"
        echo e.toString()
        throw e
    }
    try
    {
         //env = "${config.env}
      //echo '${config.env}'
       //def env = "testing"
        echo "${config.env}"
        sh "aws s3 cp s3://da-app-configuration/${config.env}/web/application/gulpfile.js ."
    }
    catch (Exception e)
    {
         echo "Failed while Code Build"
         echo e.toString()
         throw e
    }
}
  
