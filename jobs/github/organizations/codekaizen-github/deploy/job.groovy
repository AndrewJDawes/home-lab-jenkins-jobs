/**********************************************************************************************************
organizationFolder documentation: https://jenkins-development.webfxinteractive.com/plugin/job-dsl/api-viewer/index.html#path/javaposse.jobdsl.dsl.DslFactory.organizationFolder
*************************************************************************************************************/

//Name of the job, should be lowercase, dash delimited.
organizationFolder('jobs-v1/github/organizations/codekaizen-github/deploy') {

    displayName("CodeKaizen GitHub Deploy")

    organizations {
        github {
            apiUri("https://api.github.com")
            repoOwner("codekaizen-github")
            credentialsId("github-app-organization-codekaizen")
            traits {
                //Which repos to specifically include/exclude
                // sourceWildcardFilter {
                //     includes("*jenkins-example-action-rsync-deployment")
                //     excludes("")
                // }
                //Which branches to specifically include/exclude
                gitHubBranchDiscovery {
                    strategyId(3) //3 = All branches: Ignores whether the branch is also filed as a pull request and instead discovers all branches on the origin repository.
                }
                //Which PRs to specifically include/exclude from forks
                // gitHubForkDiscovery {
                //     strategyId(2) //2 = The current pull request revision
                //     trust {
                //         gitHubTrustPermissions() //From users with Admin or Write permission
                //     }
                // }
                // //Which PRs to specifically include/exclude from origin
                // gitHubPullRequestDiscovery {
                //     strategyId(2) //2 = The current pull request revision
                // }
                headWildcardFilter {
                    includes("deploy/*")
                    excludes("")
                    // excludes("modified-files*")
                }
            }
        }
    }

    projectFactories {
        workflowMultiBranchProjectFactory {
            //Include the repo's branch in the Organization folder if there is a Jenkinsfile here
            scriptPath('.jenkins/deploy/Jenkinsfile')
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
