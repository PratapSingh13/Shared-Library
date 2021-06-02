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
        echo "${config.bucket_name}"
        sh "aws s3 cp s3://${config.bucket_name}/${config.env}/web/application/gulpfile.js ."
    }
    catch (Exception e)
    {
         echo "Failed while Code Build"
         echo e.toString()
         throw e
    }
}
  
