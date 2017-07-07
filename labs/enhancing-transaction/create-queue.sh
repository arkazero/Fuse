#!/bin/sh

JBOSS_HOME=$HOME/opt/jboss-eap-6.4

$JBOSS_HOME/bin/jboss-cli.sh --connect <<EOF
/subsystem=messaging/hornetq-server=default/address-setting=jms.queue.ContactsQueue:add(max-delivery-attempts=1)
jms-queue add --queue-address=ContactsQueue --entries=java:/jms/queue/ContactsQueue
/subsystem=messaging/hornetq-server=default/address-setting=jms.queue.DeadLetter:add
jms-queue add --queue-address=DeadLetter --entries=java:/jms/queue/DeadLetter
EOF
