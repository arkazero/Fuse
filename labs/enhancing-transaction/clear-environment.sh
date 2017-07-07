#!/bin/sh

JBOSS_HOME=$HOME/opt/jboss-eap-6.4

$JBOSS_HOME/bin/jboss-cli.sh --connect <<EOF
batch
/subsystem=messaging/hornetq-server=default/address-setting=jms.queue.ContactsQueue:remove-messages
/subsystem=messaging/hornetq-server=default/address-setting=jms.queue.ContactsQueue:reset-message-counter
/subsystem=messaging/hornetq-server=default/address-setting=jms.queue.ContactsQueue:remove(max-delivery-attempts=1)
jms-queue remove --queue-address=ContactsQueue
/subsystem=messaging/hornetq-server=default/address-setting=jms.queue.DeadLetter:remove-messages
/subsystem=messaging/hornetq-server=default/address-setting=jms.queue.DeadLetter:reset-message-counter
/subsystem=messaging/hornetq-server=default/address-setting=jms.queue.DeadLetter:remove
jms-queue remove --queue-address=DeadLetter
/subsystem=messaging/hornetq-server=default/jms-queue=DLQ:remove-messages
/subsystem=messaging/hornetq-server=default/jms-queue=DLQ:reset-message-counter
run-batch
EOF

mysql -ubookstore -predhat -h infrastructure bookstore -e "DELETE from Contact"

