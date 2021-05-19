def call (Map stepParams)
{
    stage('Cloning Packer Code')
    {
        try {
            checkout scm
            //git branch: 'develop', credentialsId: 'Internal-GitLab', url: 'git@gitlab.dainternal.com:devops/infrastructure/monolith/packer/goldenami.git'
        }
        catch(Exception e)  {
            echo "slack /email notifcation"
        }
    }
    config = readYaml file: "${stepParams.file}"
    echo "$config"
    stage('Validating Packer code')
    {
        try {
        sh "packer validate -var-file=$config.variable_file $config.packerBuilder_file"
        }
        catch(Exception e) {
            echo "slack /email notifcation"
        }
    }
    stage('Creating AMI')
    {
        try {
        sh "packer build -var-file=$config.variable_file  $config.packerBuilder_file"
        }
        catch(Exception e) {
            echo "slack /email notifcation"
        }
    }
}
