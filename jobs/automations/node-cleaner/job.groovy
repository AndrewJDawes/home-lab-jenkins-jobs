/**********************************************************************************************************
  Pipeline Job Template

  Use this template to create a Pipeline Job from code using the Job DSL plugin.
  https://plugins.jenkins.io/job-dsl/

*************************************************************************************************************/

// Name of the job, should be lowercase, dash delimited.
pipelineJob('jobs-v2/automations/node-cleaner') {
    // Provide a more human readable name for the job here.
    displayName("Node Cleaner")

    // SCM definition
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://github.com/codekaizen-github/jenkins-targeted-nodes-sh.git')
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
        // NODE_LABEL
        stringParam('NODE_LABEL', 'docker')
        // SCRIPT
        // Prune
        stringParam('SCRIPT', 'docker system prune -f -a')
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
