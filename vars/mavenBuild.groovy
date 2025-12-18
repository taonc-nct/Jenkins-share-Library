def build() {
    // Checkout Git repo từ pipeline SCM
    checkout scm

    // Debug xem đã có code chưa
    sh 'pwd'
    sh 'ls -la'

    // Build Maven
    def mvnCommand = 'mvn clean package -Dmaven.test.skip=true'
    def exitCode = sh(script: mvnCommand, returnStatus: true)

    if (exitCode != 0) {
        error "Maven build failed with exit code: ${exitCode}"
    }
}

return this
