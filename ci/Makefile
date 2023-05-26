THIS_FILE := $(lastword $(MAKEFILE_LIST))

.PHONY: clean build publish
SHELL := /bin/bash

# if no CI flag is specified, make docker calls interactive
DOCKER_ARGS := $(ADDITIONAL_DOCKER_ARGS)
ifeq ($(CI),)
	DOCKER_ARGS += -it
endif
ifneq ($(TARGET_NEXUS),)
	DOCKER_ARGS += -e TARGET_NEXUS=$(TARGET_NEXUS)
endif
ifneq ($(DO_SIGN),)
	DOCKER_ARGS += -e DO_SIGN=$(DO_SIGN)
endif
ifneq ($(ARCHIVES_NAME_SUFFIX),)
	DOCKER_ARGS += -e ARCHIVES_NAME_SUFFIX=$(ARCHIVES_NAME_SUFFIX)
endif

UID ?= $(shell id -u)
GID ?= $(shell id -g)

clean:
	./gradlew clean

build:
ifneq ($(ARCHIVES_NAME_SUFFIX),)
	$(eval NAME_SUFFIX_FLAG := -ParchivesNameSuffix=$(ARCHIVES_NAME_SUFFIX))
endif
	./gradlew build $(NAME_SUFFIX_FLAG)

ifeq ($(DO_SIGN), true)
SIGN_FLAG = -PdoSign
SIGN_FLAG += -Psigning.keyId=$(shell gpg -K | grep -v secring.gpg | grep sec | awk '{print $$2}' | sed 's|.*/||g')
SIGN_FLAG += -Psigning.secretKeyRingFile=/home/jenkins/.gnupg/secring.gpg
SIGN_FLAG += -Psigning.password=
endif
publish:
ifeq ($(TARGET_NEXUS),)
	$(error TARGET_NEXUS must be set)
endif
ifneq ($(ARCHIVES_NAME_SUFFIX),)
	$(eval NAME_SUFFIX_FLAG := -ParchivesNameSuffix=$(ARCHIVES_NAME_SUFFIX))
endif
	./gradlew publish $(NAME_SUFFIX_FLAG) -PtargetNexus=$(TARGET_NEXUS) $(SIGN_FLAG) -PnexusUsername=$(NEXUS_USERNAME) -PnexusPassword=$(NEXUS_PASSWORD) --stacktrace

clean_build: clean build

bash:
	bash

clean_docker:
	rm -f ci/Dockerfile.tag

DOCKER_IMAGE := docker.h2o.ai/opsh2oai/xgboost-predictor-build
ci/Dockerfile.tag: ci/Dockerfile
	$(info Building docker image, git credentials will be required.)
	echo $(DOCKER_IMAGE) > ci/Dockerfile.tag
	docker build \
		-t $(DOCKER_IMAGE) \
		-f ci/Dockerfile \
		.

publish_in_docker: ci/Dockerfile.tag
ifeq ($(SECRING_PATH),)
	$(error SECRING_PATH must be set.)
endif
ifeq ($(NEXUS_USERNAME),)
	$(error NEXUS_USERNAME must be set.)
endif
ifeq ($(NEXUS_PASSWORD),)
	$(error NEXUS_PASSWORD must be set.)
endif
	docker run --rm \
		-u jenkins:jenkins \
		-v `pwd`:/workspace \
		-v $(SECRING_PATH):/secring.gpg:ro \
		-w /workspace \
		--entrypoint /bin/bash \
		--add-host=nexus:172.17.0.53 \
		-e NEXUS_USERNAME=$(NEXUS_USERNAME) \
		-e NEXUS_PASSWORD=$(NEXUS_PASSWORD) \
		$(DOCKER_ARGS) \
		$(DOCKER_IMAGE) \
		-c "gpg --import /secring.gpg && \
			make -f ci/Makefile publish"

%_in_docker: ci/Dockerfile.tag
	docker run --rm \
		-u $(UID):$(GID) \
		-v `pwd`:/workspace \
		-w /workspace \
		--entrypoint /bin/bash \
		$(DOCKER_ARGS) \
		$(DOCKER_IMAGE) \
		-c "make -f ci/Makefile $*"

run_in_docker: ci/Dockerfile.tag
	docker run --rm -it \
		-v `pwd`:/workspace \
		-v $(SECRING_PATH):/secring.gpg:ro \
		-w /workspace \
		--entrypoint /bin/bash \
		--add-host=nexus:172.17.0.53 \
		-e NEXUS_USERNAME=$(NEXUS_USERNAME) \
		-e NEXUS_PASSWORD=$(NEXUS_PASSWORD) \
		$(DOCKER_ARGS) \
		$(DOCKER_IMAGE)

# Public release commands
NETRC := netrc.txt
OSSRH_STAGING := https://oss.sonatype.org/service/local/staging
CURL := curl -v --netrc-file $(NETRC) --fail
start_public_release:
ifeq ($(SECRING_PATH),)
	$(error SECRING_PATH must be set.)
endif
ifeq ($(NEXUS_USERNAME),)
	$(error NEXUS_USERNAME must be set.)
endif
ifeq ($(NEXUS_PASSWORD),)
	$(error NEXUS_PASSWORD must be set.)
endif
ifeq ($(VERSION),)
	$(error VERSION must be set.)
endif
ifeq ($(INITIATOR_EMAIL),)
	$(error VERSION must be set.)
endif
	echo "machine oss.sonatype.org login $(NEXUS_USERNAME) password $(NEXUS_PASSWORD)" > $(NETRC)
	$(CURL) -H "accept: application/json" -H "content-type: application/json" "$(OSSRH_STAGING)/profiles/3ce9ff9ad792da/start" -d '{"data": {"description":"Release xgboost-predictor:$(VERSION) started by $(INITIATOR_EMAIL)"} }' > staging-start-response2.json

import_files_to_public_release:
ifeq ($(SECRING_PATH),)
	$(error SECRING_PATH must be set.)
endif
ifeq ($(NEXUS_USERNAME),)
	$(error NEXUS_USERNAME must be set.)
endif
ifeq ($(NEXUS_PASSWORD),)
	$(error NEXUS_PASSWORD must be set.)
endif
ifeq ($(VERSION),)
	$(error VERSION must be set.)
endif
ifeq ($(STAGING_REPOSITORY_ID),)
	$(error STAGING_REPOSITORY_ID must be set.)
endif
	#$(CURL) "$(OSSRH_STAGING)/profiles/3ce9ff9ad792da"
	$(CURL) "$(OSSRH_STAGING)/deployByRepositoryId/$(STAGING_REPOSITORY_ID)/ai/h2o/xgboost-predictor/$(VERSION)/" --upload-file "{build/repo/ai/h2o/xgboost-predictor/$(VERSION)/xgboost-predictor-$(VERSION).jar,build/repo/ai/h2o/xgboost-predictor/$(VERSION)/xgboost-predictor-$(VERSION).jar.asc,build/repo/ai/h2o/xgboost-predictor/$(VERSION)/xgboost-predictor-$(VERSION).jar.asc.md5,build/repo/ai/h2o/xgboost-predictor/$(VERSION)/xgboost-predictor-$(VERSION).jar.sha1,build/repo/ai/h2o/xgboost-predictor/$(VERSION)/xgboost-predictor-$(VERSION).jar.md5,build/repo/ai/h2o/xgboost-predictor/$(VERSION)/xgboost-predictor-$(VERSION).jar.sha1,build/repo/ai/h2o/xgboost-predictor/$(VERSION)/xgboost-predictor-$(VERSION).pom,build/repo/ai/h2o/xgboost-predictor/$(VERSION)/xgboost-predictor-$(VERSION).pom.asc,build/repo/ai/h2o/xgboost-predictor/$(VERSION)/xgboost-predictor-$(VERSION).pom.asc.md5,build/repo/ai/h2o/xgboost-predictor/$(VERSION)/xgboost-predictor-$(VERSION).pom.asc.sha1,build/repo/ai/h2o/xgboost-predictor/$(VERSION)/xgboost-predictor-$(VERSION).pom.md5,build/repo/ai/h2o/xgboost-predictor/$(VERSION)/xgboost-predictor-$(VERSION).pom.sha1,build/repo/ai/h2o/xgboost-predictor/$(VERSION)/xgboost-predictor-$(VERSION)-javadoc.jar,build/repo/ai/h2o/xgboost-predictor/$(VERSION)/xgboost-predictor-$(VERSION)-javadoc.jar.asc,build/repo/ai/h2o/xgboost-predictor/$(VERSION)/xgboost-predictor-$(VERSION)-javadoc.jar.asc.md5,build/repo/ai/h2o/xgboost-predictor/$(VERSION)/xgboost-predictor-$(VERSION)-javadoc.jar.asc.sha1,build/repo/ai/h2o/xgboost-predictor/$(VERSION)/xgboost-predictor-$(VERSION)-javadoc.jar.md5,build/repo/ai/h2o/xgboost-predictor/$(VERSION)/xgboost-predictor-$(VERSION)-javadoc.jar.sha1,build/repo/ai/h2o/xgboost-predictor/$(VERSION)/xgboost-predictor-$(VERSION)-sources.jar,build/repo/ai/h2o/xgboost-predictor/$(VERSION)/xgboost-predictor-$(VERSION)-sources.jar.asc,build/repo/ai/h2o/xgboost-predictor/$(VERSION)/xgboost-predictor-$(VERSION)-sources.jar.asc.md5,build/repo/ai/h2o/xgboost-predictor/$(VERSION)/xgboost-predictor-$(VERSION)-sources.jar.asc.sha1,build/repo/ai/h2o/xgboost-predictor/$(VERSION)/xgboost-predictor-$(VERSION)-sources.jar.md5,build/repo/ai/h2o/xgboost-predictor/$(VERSION)/xgboost-predictor-$(VERSION)-sources.jar.sha1}"
	$(CURL) "$(OSSRH_STAGING)/deployByRepositoryId/$(STAGING_REPOSITORY_ID)/ai/h2o/h2o-tree-api/$(VERSION)/" --upload-file "{build/repo/ai/h2o/h2o-tree-api/$(VERSION)/h2o-tree-api-$(VERSION).jar,build/repo/ai/h2o/h2o-tree-api/$(VERSION)/h2o-tree-api-$(VERSION).jar.asc,build/repo/ai/h2o/h2o-tree-api/$(VERSION)/h2o-tree-api-$(VERSION).jar.asc.md5,build/repo/ai/h2o/h2o-tree-api/$(VERSION)/h2o-tree-api-$(VERSION).jar.sha1,build/repo/ai/h2o/h2o-tree-api/$(VERSION)/h2o-tree-api-$(VERSION).jar.md5,build/repo/ai/h2o/h2o-tree-api/$(VERSION)/h2o-tree-api-$(VERSION).jar.sha1,build/repo/ai/h2o/h2o-tree-api/$(VERSION)/h2o-tree-api-$(VERSION).pom,build/repo/ai/h2o/h2o-tree-api/$(VERSION)/h2o-tree-api-$(VERSION).pom.asc,build/repo/ai/h2o/h2o-tree-api/$(VERSION)/h2o-tree-api-$(VERSION).pom.asc.md5,build/repo/ai/h2o/h2o-tree-api/$(VERSION)/h2o-tree-api-$(VERSION).pom.asc.sha1,build/repo/ai/h2o/h2o-tree-api/$(VERSION)/h2o-tree-api-$(VERSION).pom.md5,build/repo/ai/h2o/h2o-tree-api/$(VERSION)/h2o-tree-api-$(VERSION).pom.sha1,build/repo/ai/h2o/h2o-tree-api/$(VERSION)/h2o-tree-api-$(VERSION)-javadoc.jar,build/repo/ai/h2o/h2o-tree-api/$(VERSION)/h2o-tree-api-$(VERSION)-javadoc.jar.asc,build/repo/ai/h2o/h2o-tree-api/$(VERSION)/h2o-tree-api-$(VERSION)-javadoc.jar.asc.md5,build/repo/ai/h2o/h2o-tree-api/$(VERSION)/h2o-tree-api-$(VERSION)-javadoc.jar.asc.sha1,build/repo/ai/h2o/h2o-tree-api/$(VERSION)/h2o-tree-api-$(VERSION)-javadoc.jar.md5,build/repo/ai/h2o/h2o-tree-api/$(VERSION)/h2o-tree-api-$(VERSION)-javadoc.jar.sha1,build/repo/ai/h2o/h2o-tree-api/$(VERSION)/h2o-tree-api-$(VERSION)-sources.jar,build/repo/ai/h2o/h2o-tree-api/$(VERSION)/h2o-tree-api-$(VERSION)-sources.jar.asc,build/repo/ai/h2o/h2o-tree-api/$(VERSION)/h2o-tree-api-$(VERSION)-sources.jar.asc.md5,build/repo/ai/h2o/h2o-tree-api/$(VERSION)/h2o-tree-api-$(VERSION)-sources.jar.asc.sha1,build/repo/ai/h2o/h2o-tree-api/$(VERSION)/h2o-tree-api-$(VERSION)-sources.jar.md5,build/repo/ai/h2o/h2o-tree-api/$(VERSION)/h2o-tree-api-$(VERSION)-sources.jar.sha1}"
	$(CURL) "$(OSSRH_STAGING)/deployByRepositoryId/$(STAGING_REPOSITORY_ID)/ai/h2o/h2o-tree-api/" --upload-file "{build/repo/ai/h2o/h2o-tree-api/maven-metadata.xml,build/repo/ai/h2o/h2o-tree-api/maven-metadata.xml.md5,build/repo/ai/h2o/h2o-tree-api/maven-metadata.xml.sha1}"
	$(CURL) "$(OSSRH_STAGING)/deployByRepositoryId/$(STAGING_REPOSITORY_ID)/ai/h2o/xgboost-predictor/" --upload-file "{build/repo/ai/h2o/xgboost-predictor/maven-metadata.xml,build/repo/ai/h2o/xgboost-predictor/maven-metadata.xml.md5,build/repo/ai/h2o/xgboost-predictor/maven-metadata.xml.sha1}"

