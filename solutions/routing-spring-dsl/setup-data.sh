#!/bin/sh

HOST=infrastructure.lab.example.com
USER1=ftpuser1
PASS1=w0rk1n
USER2=ftpuser2
PASS2=w0rk0ut
USER3=ftpuser3
PASS3=w0rk

# just make sure the remote ftp folders are empty

echo 'Cleaning remote FTP folders...'

function cleanftp {
	USER=$1
	PASS=$2
	# This avoid an error message if you try mrm * in an empty remote folder
	REMOTE_FILES=$(lftp -c "open ${HOST} -u ${USER},${PASS} ; ls")
	if [ "$REMOTE_FILES" != "" ]; then
		lftp -c "open ${HOST} -u ${USER},${PASS} ; mrm *" &>/dev/null
	fi
}

cleanftp $USER1 $PASS1
cleanftp $USER2 $PASS2
cleanftp $USER3 $PASS3

