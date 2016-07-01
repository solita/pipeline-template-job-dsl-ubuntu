import util.AnsibleVars;

folder('Provisioning')
folder('Provisioning/Prod')

job('Provisioning/Prod/Provision') {
    deliveryPipelineConfiguration('Prod Env', 'Provision')
    quietPeriod(0)
    wrappers {
        buildName('$PIPELINE_VERSION')
        timestamps()
        preBuildCleanup()
    }
    steps {
        copyArtifacts('Provisioning/Build/Checkout') {
            buildSelector() {
                upstreamBuild(true)
            }
            includePatterns('**/*')
        }
        copyArtifacts('Provisioning/Build/Provision') {
            buildSelector() {
                upstreamBuild(true)
            }
            includePatterns('imagination/jenkins_id_rsa.pub')
        }
        shell("ansible-playbook -i '${AnsibleVars.INVENTORY_ROOT}/prod/inventory' site.yml")
    }
}
