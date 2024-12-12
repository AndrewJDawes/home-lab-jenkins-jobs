/**********************************************************************************************************
organizationFolder documentation: https://jenkins-development.webfxinteractive.com/plugin/job-dsl/api-viewer/index.html#path/javaposse.jobdsl.dsl.DslFactory.organizationFolder
*************************************************************************************************************/

//Name of the job, should be lowercase, dash delimited.
organizationFolder('jobs-v2/github/organizations/andrewjdawes/test') {

    displayName("AndrewJDawes GitHub Test")

    organizations {
        github {
            apiUri("https://api.github.com")
            repoOwner("AndrewJDawes")
            credentialsId("github-app-user-andrewjdawes")
            traits {
                //Which repos to specifically include/exclude
                // sourceWildcardFilter {
                //     includes("*jenkins-example-action-rsync-deployment")
                //     excludes("")
                // }
                //Which branches to specifically include/exclude
                gitHubBranchDiscovery {
                    strategyId(3) //1 = Exclude branches that are also filled as PRs
                }

                //Which PRs to specifically include/exclude from forks
                gitHubForkDiscovery {
                    strategyId(2) //2 = The current pull request revision
                    trust {
                        gitHubTrustPermissions() //From users with Admin or Write permission
                    }
                }
                // //Which PRs to specifically include/exclude from origin
                gitHubPullRequestDiscovery {
                    strategyId(2) //2 = The current pull request revision
                }
                headRegexFilter {
                    regex("deploy\\/.*|PR-[0-9]+")
                }
                // headWildcardFilter {
                //     includes("deploy/*")
                //     excludes("")
                //     // excludes("modified-files*")
                // }
            }
        }
    }

    projectFactories {
        workflowMultiBranchProjectFactory {
            //Include the repo's branch in the Organization folder if there is a Jenkinsfile here
            scriptPath('.jenkins/test/Jenkinsfile')
        }
    }

    triggers {
        periodicFolderTrigger {
            interval("10m")
        }
    }

    //keep old job logs
    orphanedItemStrategy {
        discardOldItems {
            numToKeep(5)
        }
    }
}
