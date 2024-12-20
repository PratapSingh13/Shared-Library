#!/usr/bin/env groovy
def provisionReporting() {
    def nodelabel = "master"
    node(nodelabel){
        stage("Initializing Job Properties") {
            checkout scm
        }
        String[] arr = [ "https://github.com/PratapSingh13/Java.git", "https://github.com/PratapSingh13/Shared-Library.git"]
        stage('Cloning and Scan Repository'){
           sh "rm -rf *"
           sh "mkdir secret_scan"
           for (repo in arr) {
               split_url = repo.split('/')
               repo_name =  split_url[split_url.length-1]
               split_repo = repo_name.split('.git')
               project = split_repo[0]
               sh "rm -rf AWS"
               sh "mkdir AWS"
               dir("AWS") {
                    checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[credentialsId: 'GitHub', url: "${repo}"]]])
                    sh label: '', script: '{ git secrets --scan; } 2> secretkeys.txt | true'
                    sh 'pwd'
                    def count = sh(script: 'cat secretkeys.txt | wc -l', returnStdout: true).trim()
                    sh 'cat secretkeys.txt'
                    count = sh(script: 'cat secretkeys.txt')
                    if(count != "1")
                    sh "cp secretkeys.txt ../secret_scan/${project}.txt"
               }
            }
        }
        stage('Mail report'){
           dir("secret_scan"){
             def count = sh(script: 'ls | wc -l', returnStdout: true).trim()
              if(count != "0")
             emailext attachmentsPattern: "*", body: "Hi Team,\nI have scanned chqbook projects to check existence of secret keys in repository and find out that there are a lot of places where we are using access and secret keys.\nPlease find the attachment with the name based on the repository for more information.",
               subject: "AWS Access and Secret Key commits in Git Repo",
              to: "yogendrapratapsingh70@gmail.com"
            }
        }
    }
}
