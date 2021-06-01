#!/usr/bin/env groovy
def provisionReporting(Map stepParams) 
{
    git_url = stepParams.git_url
    stage('Credentials Scanning')
    {
        split_url = repo_url.split('/')
        repo_name = split_url[split_url.length-1]
        split_repo = repo_name.split('.git')
        project = split_repo[0]
        sh "echo $project"
        stage ('Scanning Repository')
        {
            try
            {
                sh label: '', script: 'git secrets --install'
                sh label: '', script: 'git secrets --register-aws'
                sh label: '', script: '{ git secrets --scan; } 2> secretkeys.txt | true'
                sh "cp secretkeys.txt ${project}.txt"
                sh "cat ${project}.txt"
            }
            catch(Exception e)
            {
                echo "Failed while Credentials Scanning"
                echo e.toString()
                throw e
            }
        }
    }
}
