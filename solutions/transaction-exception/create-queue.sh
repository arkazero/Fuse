#!/bin/sh

JBOSS_HOME=$HOME/opt/jboss-eap-6.4

$JBOSS_HOME/bin/jboss-cli.sh --connect <<EOF
batch
/subsystem=messaging/hornetq-server=default/address-setting=jms.queue.OrdersQueue:add(max-delivery-attempts=1)
jms-queue add --queue-address=OrdersQueue --entries=java:/jms/queue/OrdersQueue
/subsystem=messaging/hornetq-server=default/address-setting=jms.queue.DeadLetter:add
jms-queue add --queue-address=DeadLetter --entries=java:/jms/queue/DeadLetter
run-batch
EOF
