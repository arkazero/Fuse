#!/bin/bash

HOST=infrastructure.lab.example.com
USER=ftpuser1
PASS=w0rk1n
 
echo 'Cleaning remote folder...'
REMOTE_FILES=$(lftp -c "open ${HOST} -u ${USER},${PASS} ; ls" | grep -v lost+found)
if [ "$REMOTE_FILES" != "" ]; then
	lftp -c "open ${HOST} -u ${USER},${PASS} ; mrm *" &>/dev/null
fi

