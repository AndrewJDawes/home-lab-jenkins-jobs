/**********************************************************************************************************
  Pipeline Job Template

  Use this template to create a Pipeline Job from code using the Job DSL plugin.
  https://plugins.jenkins.io/job-dsl/

*************************************************************************************************************/

// Name of the job, should be lowercase, dash delimited.
pipelineJob('jobs-v2/automations/home-lab-conjur-backup') {
    // Provide a more human readable name for the job here.
    displayName("Home Lab Conjur Backup")

    // SCM definition
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://github.com/AndrewJDawes/home-lab-conjur-backup.git')
                        // credentials('github-app-doer') // Optional: If using credentials
                    }
                    // branches('refs/heads/main') // Change the branch if necessary
                    branch('refs/tags/v1') // Change the branch if necessary
                    scriptPath('Jenkinsfile') // Path to your Jenkinsfile in the repository
                }
            }
            lightweight(true) // Optional: to use lightweight checkout
        }
    }

    // Adds build triggers to the job.
    triggers {
        // This runs the job daily at midnight
        cron {
            spec('@midnight')
            // every minute
            // spec('* * * * *')
        }
    }

    parameters {
        stringParam('REGISTRY_URL', 'https://ghcr.io')
        stringParam('REGISTRY_CREDENTIALS_ID', 'jenkins-github-ghcr-pat-token-andrewjdawes-username-password')
        stringParam('IMAGE', 'andrewjdawes/home-lab-conjur-backup:v1')
        stringParam('SSH_KEY_CREDENTIALS_ID', 'application-deployment-previously-authorized-private-key')
    }

    properties {
        buildDiscarder {
            strategy {
                logRotator {
                    numToKeepStr('3')
                    // These other arguments are still required. So set to -1 unless you need these.
                    daysToKeepStr('-1')
                    artifactDaysToKeepStr('-1')
                    artifactNumToKeepStr('-1')
                }
            }
        }
    }
}
