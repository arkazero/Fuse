#!/bin/sh

JBOSS_HOME=$HOME/opt/jboss-eap-6.4

$JBOSS_HOME/bin/jboss-cli.sh --connect <<EOF
/subsystem=messaging/hornetq-server=default/jms-queue=abc:remove
EOF

