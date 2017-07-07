#!/bin/bash

# has to be run from the solutions folder

mvn archetype:generate -B \
	-DarchetypeGroupId=org.apache.camel.archetypes \
	-DarchetypeArtifactId=camel-archetype-spring \
	-DarchetypeVersion=2.17.0.redhat-630187 \
	-DgroupId=com.redhat.training.jb421 \
	-DartifactId=eap-mgmt

