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
}
