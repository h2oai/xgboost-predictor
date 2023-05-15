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
