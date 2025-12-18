def build() {
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
