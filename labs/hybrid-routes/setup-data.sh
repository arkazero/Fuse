#!/bin/bash

HOST=infrastructure.lab.example.com
USER=ftpuser1
PASS=w0rk1n
FOLDERS="abc orly namming"
ORDERS=( 2,4,6 1,5 3 )
 
echo 'Preparing test data:'
echo '  Deleting old journal files...'
rm -rf orders
mkdir -p orders

echo '  Beginning file upload...'
n=0
for folder in $FOLDERS; do
	#echo "${n}: ${folder}: ${ORDERS[${n}]}"
	# This avoid an error message if you try rm a non-existent remote folder
	REMOTE_FILES=$(lftp -c "open ${HOST} -u ${USER},${PASS} ; cd ${folder}" 2>/dev/null)
	if [ "$REMOTE_FILES" = "" ]; then
		lftp -c "open ${HOST} -u ${USER},${PASS} ; rm -r ${folder}" &>/dev/null
	fi

	lftp -c "open ${HOST} -u ${USER},${PASS} ; mkdir ${folder} ; cd ${folder} ; mput ../../data/orders/order-[${ORDERS[${n}]}].xml" &>/dev/null
	n=$(( $n + 1 ))
done
echo '  Upload complete!'
