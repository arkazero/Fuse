#!/bin/sh

JBOSS_HOME=$HOME/opt/jboss-eap-6.4

$JBOSS_HOME/bin/jboss-cli.sh --connect <<EOF | grep message-count
/subsystem=messaging/hornetq-server=default/jms-queue=OrdersQueue:read-resource(include-runtime=true)
EOF

