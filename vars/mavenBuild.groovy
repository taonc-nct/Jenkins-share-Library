def build() {

    def mvncheck = '''
        echo "=== DEBUG WORKSPACE ==="
        pwd
        ls -la
        echo "=== FIND pom.xml ==="
        find . -name pom.xml
    '''
    def exitCodecheck = sh(script: mvncheck, returnStatus: true)

    if (exitCodecheck != 0) {
        error "Không tìm thấy pom.xml hoặc workspace có vấn đề"
    }

    def mvnCommand = '''
        mvn -B \
        -Dmaven.repo.local=$WORKSPACE/.m2/repository \
        clean install \
        -DskipTests
    '''
    def exitCode = sh(script: mvnCommand, returnStatus: true)

    if (exitCode != 0) {
        error "Maven build failed with exit code: ${exitCode}"
    }
}
