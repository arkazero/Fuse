#!/bin/sh

export SSHPASS=admin
SSH_FUSE="sshpass -e ssh -p 8101 admin@localhost"

QUEUES="abc orly namming"

for queue in ${QUEUES}; do
	echo "queue ${queue}:"
	${SSH_FUSE} "activemq:query -QQueue=${queue}" 2>/dev/null | grep QueueSize
done
