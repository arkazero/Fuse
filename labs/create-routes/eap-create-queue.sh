#!/bin/sh

JBOSS_HOME=$HOME/opt/jboss-eap-6.4

QUEUES="abc orly namming"

for queue in ${QUEUES}; do
	$JBOSS_HOME/bin/jboss-cli.sh --connect <<EOF
/subsystem=messaging/hornetq-server=default/jms-queue=${queue}:add(entries=["java:/${queue}"])
EOF

done
