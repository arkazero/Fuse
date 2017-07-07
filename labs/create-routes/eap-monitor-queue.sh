#!/bin/sh

JBOSS_HOME=$HOME/opt/jboss-eap-6.4

QUEUES="abc orly namming"

for queue in ${QUEUES}; do
	echo "queue ${queue}:"
	$JBOSS_HOME/bin/jboss-cli.sh --connect <<EOF | grep message-count
/subsystem=messaging/hornetq-server=default/jms-queue=${queue}:read-resource(include-runtime=true)
EOF

done
