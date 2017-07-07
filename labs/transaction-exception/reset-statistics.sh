#!/bin/sh

JBOSS_HOME=$HOME/opt/jboss-eap-6.4

echo "Removing queues on EAP"
$JBOSS_HOME/bin/jboss-cli.sh --connect <<EOF
batch
/subsystem=messaging/hornetq-server=default/jms-queue=OrdersQueue:remove-messages
/subsystem=messaging/hornetq-server=default/jms-queue=OrdersQueue:reset-message-counter
/subsystem=messaging/hornetq-server=default/jms-queue=DeadLetter:remove-messages
/subsystem=messaging/hornetq-server=default/jms-queue=DeadLetter:reset-message-counter
/subsystem=messaging/hornetq-server=default/jms-queue=DLQ:remove-messages
/subsystem=messaging/hornetq-server=default/jms-queue=DLQ:reset-message-counter
run-batch
EOF
echo "Removing inserted data from database"
mysql -ubookstore -predhat -h infrastructure bookstore -e "DELETE from OrderItem"
mysql -ubookstore -predhat -h infrastructure bookstore -e "DELETE from order_ where discount is not null"

echo "Clean up done!"
