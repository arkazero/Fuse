#!/bin/bash

HOST=infrastructure.lab.example.com
USER=ftpuser1
PASS=w0rk1n
FOLDERS="abc orly namming"
ORDERS=( 2,4,6 1,5 3 )
 
echo 'Preparing test data:'
echo '  Deleting old journal files...'
rm -rf /tmp/orders
mkdir -p /tmp/orders

echo '  Beginning file upload...'
n=0
for folder in $FOLDERS; do
	#echo "${n}: ${folder}: ${ORDERS[${n}]}"
	# This avoid an error message if you try rm a non-existent remote folder
	REMOTE_FILES=$(lftp -c "open ${HOST} -u ${USER},${PASS} ; cd ${folder}")
	if [ "$REMOTE_FILES" = "" ]; then
		lftp -c "open ${HOST} -u ${USER},${PASS} ; rm -r ${folder}"
	fi

	lftp -c "open ${HOST} -u ${USER},${PASS} ; mkdir ${folder} ; cd ${folder} ; mput ../../data/orders/order-[${ORDERS[${n}]}].xml"
	n=$(( $n + 1 ))
done
echo '  Upload complete!'
