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
    stage('Code Build')
    {
        try
        {
            sh 'sudo swapoff -a'
            sh 'sudo /bin/dd if=/dev/zero of=/var/swap.1 bs=1M count=1024'
            sh 'sudo /sbin/mkswap /var/swap.1'
            sh 'sudo /sbin/swapon /var/swap.1'
            sh 'aws s3 cp s3://da-app-configuration/testing/web/application/gulpfile.js .'
            sh 'aws s3 cp s3://da-app-configuration/testing/web/application/environment.testing.ts ng-app/environments/'
            sh 'aws s3 cp s3://da-app-configuration/testing/web/application/php_angular.json angular.json'
            //sh 'cd php/ && rm package-lock.json'
            sh 'npm install'
            //sh 'npm install -g node@12.14.0 --force'
            //sh 'ng build --configuration=testing'
            sh 'sudo yum install libnotify -y'
            sh 'npm install -i gulp-notify'
            sh 'npm install -i notify-send'
            sh 'niv bootstrap-sass@3.3.6'
            sh 'gulp --production'
            sh 'cd /var/www/html && sudo chown apache:apache *'
            sh 'sudo cp -r * /var/www/html/testing.docasap.com'
            sh 'sudo chmod 777 /var/www/html/testing.docasap.com -R'
            sh 'cd /var/www/html/testing.docasap.com && sudo chmod 775 composer.phar && sudo chown apache:apache -R . && /usr/local/bin/composer install'
        }
        catch (Exception e)
        {
            echo "Failed while Code Build"
            echo e.toString()
            throw e
        }
    }
    stage('Unit Testing')
    {
        try
        {
            //sh 'sudo mv /var/www/html/testing.docasap.com/tests/Unit/ProviderTest.php /opt'
            //sh 'cd /var/www/html/testing.docasap.com && vendor/bin/phpunit -c phpunit.xml --coverage-html report/codeCoverage tests/'
            //sh 'cd /var/www/html/testing.docasap.com && vendor/bin/phpunit -c phpunit.xml --coverage-xml report/coverage.xml tests/'
        }
        catch (Exception e)
        {
            echo "Failed to generate report"
            echo e.toString()
            throw e
        }
    }
    stage('Checking Code-Quality')
    {
        try
        {
            def scannerHome = tool 'SonarQubeScanner';
            withSonarQubeEnv('SONAR_HOST_URL')
            {
                sh "${scannerHome}/sonar-scanner"
            }
        }
        catch (Exception e)
        {
            echo "Failed to check code quality"
            echo e.toString()
            throw e
        }
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
