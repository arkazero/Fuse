#!/bin/bash

HOST=infrastructure.lab.example.com
USER=ftpuser1
PASS=w0rk1n
FOLDERS="abc orly namming"
 
echo 'Deleting old journal files...'
rm -rf /tmp/orders
mkdir -p /tmp/orders

echo 'Cleaning remote folders...'
n=0
for folder in $FOLDERS; do
	#echo "${n}: ${folder}: ${ORDERS[${n}]}"
	# This avoid an error message if you try rm a non-existent remote folder
	REMOTE_FILES=$(lftp -c "open ${HOST} -u ${USER},${PASS} ; cd ${folder}" 2>/dev/null)
	if [ "$REMOTE_FILES" = "" ]; then
		lftp -c "open ${HOST} -u ${USER},${PASS} ; rm -r ${folder}" &>/dev/null
	fi
done
echo '  Upload complete!'
