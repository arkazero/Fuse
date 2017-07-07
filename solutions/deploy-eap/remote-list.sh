#!/bin/bash

HOST=infrastructure.lab.example.com
USER=ftpuser1
PASS=w0rk1n
FOLDERS="abc orly namming"
 
echo 'Getting remote file listing...'
for folder in $FOLDERS; do
	echo "Folder: ${folder}"
	# This avoid an error message if you try rm a non-existent remote folder
	lftp -c "open ${HOST} -u ${USER},${PASS} ; ls ${folder}"
done
echo 'Remote listing complete!'
