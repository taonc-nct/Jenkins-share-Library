package com.company.git

class GitBranchLister implements Serializable {

    def steps

    GitBranchLister(steps) {
        this.steps = steps
    }

    /**
     * List all remote branches of a Git repository
     *
     * @param repoUrl Git repository URL (https or ssh)
     * @return List of branch names (String)
     */
    List<String> listAllBranches(String repoUrl) {

        def output = steps.sh(
            script: "git ls-remote --heads ${repoUrl}",
            returnStdout: true
        ).trim()

        if (!output) {
            return []
        }

        return output
            .split("\n")
            .collect { line ->
                line.split()[1].replace("refs/heads/", "")
            }
            .sort()
    }
}
