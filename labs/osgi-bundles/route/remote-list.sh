#!/bin/bash

HOST=infrastructure.lab.example.com
USER=ftpuser1
PASS=w0rk1n
 
echo 'Getting remote file list...'
REMOTE_FILES=$(lftp -c "open ${HOST} -u ${USER},${PASS} ; ls" | grep -v lost+found)
echo "${REMOTE_FILES}"
if [ "$REMOTE_FILES" != "" ]; then
	echo 'Listing journal output...'
	lftp -c "open ${HOST} -u ${USER},${PASS} ; cat contacts.txt"
fi
