def call(Map stepParams)
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
