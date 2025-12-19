def build() {
    def mvnCommand = '''
         mvn clean install -DskipTests -Dcheckstyle.skip=true -Dmaven.repo.local=$WORKSPACE/.m2/repository 

    '''
    def exitCode = sh(script: mvnCommand, returnStatus: true)

    if (exitCode != 0) {
        error "Maven build failed with exit code: ${exitCode}"
    }
}
