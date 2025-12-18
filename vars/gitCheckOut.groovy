def call() {

    def branch = sh(script: "git rev-parse --abbrev-ref HEAD", returnStdout: true).trim()
    echo "Current branch: ${branch}"

    // Reject main và master
    if (branch == 'main' || branch == 'master') {
        error "❌ Build rejected: 'main' or 'master' branch is not allowed to run this pipeline"
    }

    // Chỉ cho phép dev* hoặc hotfix*
    if (!(branch ==~ /^dev.*$/ || branch ==~ /^hotfix.*$/)) {
        error "❌ Build rejected: Only branches starting with 'dev' or 'hotfix' are allowed. Current branch: ${branch}"
    }

    // Checkout code
    checkout([
        $class: 'GitSCM',
        branches: [[name: branch]],
        userRemoteConfigs: [[url: env.GIT_URL ?: error("GIT_URL not set")]]
    ])

    echo "Checked out branch ${branch} successfully"
}
