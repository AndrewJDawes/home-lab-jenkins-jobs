/**********************************************************************************************************
organizationFolder documentation: https://jenkins-development.webfxinteractive.com/plugin/job-dsl/api-viewer/index.html#path/javaposse.jobdsl.dsl.DslFactory.organizationFolder
*************************************************************************************************************/

//Name of the job, should be lowercase, dash delimited.
organizationFolder('jobs-v2/github/organizations/umerx-github/deploy') {

    displayName("Umerx GitHub Deploy")

    properties {
        suppressFolderAutomaticTriggering {
            branches("*")
            strategy("INDEXING")
        }
    }

    // Requires this plugin: https://plugins.jenkins.io/basic-branch-build-strategies/
    buildStrategies {
        skipInitialBuildOnFirstBranchIndexing()
        buildRegularBranches()
        buildChangeRequests {
            ignoreTargetOnlyChanges(false)
            ignoreUntrustedChanges(true)
        }
        buildTags {
            atLeastDays("0")
            atMostDays("1")
        }
        // Build branches on first index so we can use for CI/CD as part of pull requests.
        // skipInitialBuildOnFirstBranchIndexing()
        // Even if the refs are found (Job added in Jenkins), only automatically Build the branches that match the regex.
        // buildNamedBranches {
        //     filters {
        //         regex {
        //             regex("deploy/.*")
        //             caseSensitive(true)
        //         }
        //     }
        // }
    }

    organizations {
        github {
            apiUri("https://api.github.com")
            repoOwner("umerx-github")
            credentialsId("github-app-organization-umerx")
            traits {
                // Which repos to specifically include/exclude
                // sourceWildcardFilter {
                //     includes("*jenkins-example-action-rsync-deployment")
                //     excludes("")
                // }
                // Which branches to discover
                gitHubBranchDiscovery {
                    strategyId(3) //3 = All branches: Ignores whether the branch is also filed as a pull request and instead discovers all branches on the origin repository.
                }
                // //Which PRs to specifically include/exclude from origin
                gitHubPullRequestDiscovery {
                    // strategyId(1) //1 = Merges the pull request with the current target branch revision
                    // strategyId(2) //2 = The current pull request revision
                    strategyId(3) //3 = Both the current pull request revision and the pull request merged with the current target branch revision
                }
                //Which PRs to specifically include/exclude from forks
                // gitHubForkDiscovery {
                //     strategyId(2) //2 = The current pull request revision
                //     trust {
                //         gitHubTrustPermissions() //From users with Admin or Write permission
                //     }
                // }
                // Discover all tags
                gitHubTagDiscovery()
                // Other refs to discover
                // discoverOtherRefs {
                //     ref("deploy/*")
                // }
                // Filter to include/exclude specific refs. These won't even show in Jenkins. Cannot distinguish between branches and tags.
                headWildcardFilter {
                    includes("*")
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
            interval("1d")
        }
    }

    // Retain number of deleted repos and branches
    orphanedItemStrategy {
        discardOldItems {
            // daysToKeep(30)
            numToKeep(5)
        }
    }
}
