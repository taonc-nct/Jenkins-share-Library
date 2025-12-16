def build(String imageName, String imageTag) {
    try {
        def buildCmd = """
            buildah bud \
            --file ${WORKSPACE}/Dockerfile \
            --tag ${imageName}:${imageTag} \
            ${WORKSPACE}
        """

        def exitCode = sh(script: buildCmd, returnStatus: true)

        if (exitCode != 0) {
            error "Build failed with exit code: ${exitCode}"
        } else {
            echo " Build succeeded."
        }
    } catch (Exception e) {
        error "Exception during build: ${e.getMessage()}"
    }
}

def scanImage(Srting imageName, String imageTag) {
    try {
        def scanCmd = "trivy image --exit-code 1 --severity HIGH,CRITICAL ${imageName}:${imageTag}"
        def exitCode = sh(script: scanCmd, returnStatus: true)
        if (exitCode !=0) {
            error "trivy scan failed: vulnerabilities found!"
        }else{
            ehco "Trivy scan pass. No HIGH/CRITICAL vulnerabilities. "
        }
    } catch (Exception e){
        error "Exception during Trivy scan: ${e.getMessage()}"
    }
}

def push(String name_repo, String imageName, String imageTag, String registryCred) {
    withCredentials([usernamePassword(credentialsId: registryCred, usernameVariable: 'REG_USR', passwordVariable: 'REG_PSW')]) {
        try {
            // Login vào registry
            sh """
                buildah login -u ${REG_USR} -p ${REG_PSW} ${name_repo}
            """

            // Push image
            sh """
                buildah push ${imageName}:${imageTag} ${name_repo}/${imageName}:${imageTag}
            """

            echo "Push succeeded to ${name_repo}/${imageName}:${imageTag}"
        } catch (Exception e) {
            echo "Error occurred during Build push: ${e.getMessage()}"
            currentBuild.result = 'FAILURE'
            throw e
        }
    }
}

