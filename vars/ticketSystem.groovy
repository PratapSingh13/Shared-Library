def call(Map stageParams) {
    checkout([
        $class: 'GitSCM',
        branches: [[ name: stageParams.branch ]],
        userRemoteConfigs: [[ url: stageParams.url ]]
    ])
}

def build(Map stageParams) {
    
    sh "cd attendance && make"
}
