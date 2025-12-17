package com.company.git

class GitBranchValidator implements Serializable {

    def steps

    GitBranchValidator(steps) {
        this.steps = steps
    }

    /**
     * Validate deploy branch automatically from CI context
     */
    void validateDeployBranch() {

        def branch = steps.env.BRANCH_NAME ?: steps.env.GIT_BRANCH

        if (!branch) {
            steps.error("❌ Cannot determine Git branch from CI environment")
        }

        // Normalize branch name (remove origin/)
        branch = branch.replaceFirst(/^origin\//, '')

        // Block main
        if (branch == 'main') {
            steps.error("❌ Deployment from 'main' branch is NOT allowed")
        }

        // Allow only dev* or hot-fix*
        if (!(branch ==~ /dev.*/ || branch ==~ /hot-fix.*/)) {
            steps.error("""
❌ Branch '${branch}' is NOT allowed for deployment
✔ Allowed:
  - dev*
  - hot-fix*
""")
        }

        steps.echo("✅ Branch '${branch}' passed deployment validation")
    }
}
