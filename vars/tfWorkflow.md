# Terraform Workflow
I'm Shared Library for terraform workflow which will execute and manage all terraform related operations.

### Prerequisites
- [Jenkins](https://www.jenkins.io/doc/book/installing/) - Jenkins should be installed on your machine to execute this Shared Library successfully.

- [Terraform](https://www.terraform.io/) - Terraform should be installed on your machine where you are executing the code.

- [tflint](https://github.com/terraform-linters/tflint) - For Linting tflint should be installed on your machine where you are executing the code.

### Jenkins Pluggin
- Pipeline Utility Steps
- Build users vars plugin
- Slack Notification
- Google Chat Notification

### Example

****Jenkinsfile****
```groovy
@Library('Shared-Library@master')
node{
    terraformWorkflow.call(
        configFilePath: "/path_for/config.properties"
    )
}
````

****Property File****
````properties
CODE_BASE_PATH = ./
SLACK_CHANNEL_NAME = ABC
GOOGLE_CHAT_URL = https://chat.googleapis.com/v1/spaces/AAAAuvH0DTI/messages?key=AIzaSyDdI0hCZtE6vySjMm-WEfRq3CPzqKqqsHI&token=6bxkYEGZWPNi1G449fc7FCvyX7PP5v6Xx1rLo3uWLTE%3D
ENVIRONMENT = prod
````

### Parameters

|**Paramter Name**| **Type** | **Description** |
|:-----------------:|:----------:|:-----------------:|
| CODE_BASE_PATH | *mandatory* | Path of the terraform codebase which needs to be execute |
| SLACK_CHANNEL_NAME | *manadatory* | Name of the slack channel in which notification should be sent |
| GOOGLE_CHAT_URL | *manadatory* | URL of the Google Chat in which notification should be sent |
| ENVIRONMENT | *mandatory* | Environment name for which pipeline is executing |

![](../img/terraformWorkflow.png)
