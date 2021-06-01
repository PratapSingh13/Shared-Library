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
        gs.provisionReporting(
            git_url: "${config.git_url}"
        )
    }
    catch(Exception e)
    {
        echo "Unable to Scan Credentials"
        echo e.toString()
        throw e
    }
}
