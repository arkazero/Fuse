#!/bin/sh

JBOSS_HOME=$HOME/opt/jboss-eap-6.4

echo "Removing queues on EAP"
$JBOSS_HOME/bin/jboss-cli.sh --connect <<EOF
batch
/subsystem=messaging/hornetq-server=default/address-setting=jms.queue.OrdersQueue:remove(max-delivery-attempts=1)
jms-queue remove --queue-address=OrdersQueue
/subsystem=messaging/hornetq-server=default/address-setting=jms.queue.DeadLetter:remove
jms-queue remove --queue-address=DeadLetter
/subsystem=messaging/hornetq-server=default/jms-queue=DLQ:remove-messages
/subsystem=messaging/hornetq-server=default/jms-queue=DLQ:reset-message-counter
run-batch
EOF
echo "Recreating queues on EAP"
$JBOSS_HOME/bin/jboss-cli.sh --connect <<EOF
batch
/subsystem=messaging/hornetq-server=default/address-setting=jms.queue.OrdersQueue:add(max-delivery-attempts=1)
jms-queue add --queue-address=OrdersQueue --entries=java:/jms/queue/OrdersQueue
/subsystem=messaging/hornetq-server=default/address-setting=jms.queue.DeadLetter:add
jms-queue add --queue-address=DeadLetter --entries=java:/jms/queue/DeadLetter
run-batch
EOF

echo "Removing inserted data from database"
mysql -ubookstore -predhat -h infrastructure bookstore -e "DELETE from OrderItem"
mysql -ubookstore -predhat -h infrastructure bookstore -e "DELETE from order_ where discount is not null"

echo "Clean up done!"
