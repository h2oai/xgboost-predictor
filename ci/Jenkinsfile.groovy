@Library('test-shared-library@1.9') _

import ai.h2o.ci.buildsummary.StagesSummary

// initialize build summary
buildSummary('https://github.com/h2oai/xgboost-predictor', true)
// use default StagesSummary implementation
buildSummary.get().addStagesSummary(this, new StagesSummary())

properties([
    parameters([
        choice(choices: ['none', 'private', 'public'], description: 'Nexus to publish to', name: 'targetNexus')
    ])
])

def makeOpts = 'CI=1'

node ('master') {
    buildSummary.stageWithSummary('Checkout') {
        cleanWs()
        def scmEnv = checkout scm
        env.BRANCH_NAME = scmEnv.GIT_BRANCH.replaceAll('origin/', '')
        final def version = sh(script: 'cat gradle.properties | grep version | sed "s/version=//"', returnStdout: true).trim()

        def archivesNameSuffix = null
        if (env.BRANCH_NAME != 'master') {
            archivesNameSuffix = env.BRANCH_NAME.replaceAll('/|\\ ','-')
            makeOpts += " ARCHIVES_NAME_SUFFIX=${archivesNameSuffix}"
        }
        currentBuild.description = "ai.h2o:xgboost-predictor"
        if (archivesNameSuffix) {
            currentBuild.description += "-${archivesNameSuffix}"
        }
        currentBuild.description += ":${version}"
    }
    buildSummary.stageWithSummary('Prepare Docker Image and clean') {
        withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', accessKeyVariable: 'AWS_ACCESS_KEY_ID', credentialsId: 'AWS S3 Credentials', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {
                docker.image('docker.h2o.ai/s3cmd').inside("-e AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID} -e AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY}") {
                sh """
                    cd ci
                    s3cmd get s3://artifacts.h2o.ai/releases/oracle/jdk-8/x64-linux/jdk1.8.0_171.zip
                """
            }
        }
        sh "make ${makeOpts} -f ci/Makefile clean_in_docker"
    }

    buildSummary.stageWithSummary('Build') {
        sh "make ${makeOpts} -f ci/Makefile build_in_docker"
        archiveArtifacts artifacts: 'xgboost-predictor/build/libs/*.jar'
    }

    if (params.targetNexus == 'private' || params.targetNexus == 'public') {
        buildSummary.stageWithSummary("Publish to ${params.targetNexus.capitalize()} Nexus") {
            def credentialsId
            if (params.targetNexus == 'private') {
                credentialsId = 'LOCAL_NEXUS'
            } else if (params.targetNexus == 'public') {
                credentialsId = 'PUBLIC_NEXUS'
            } else {
                error "Cannot find credentials for targetNexus=${params.targetNexus}"
            }
            withCredentials([usernamePassword(credentialsId: credentialsId, usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD'),
                file(credentialsId: 'release-secret-key-ring-file', variable: 'SECRING_PATH')]) {
                sh "make ${makeOpts} TARGET_NEXUS=${params.targetNexus} DO_SIGN=true -f ci/Makefile publish_in_docker"
            }
        }
    }
}
