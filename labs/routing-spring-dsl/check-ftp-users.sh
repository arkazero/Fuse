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

function checkftpuserfolder {
	USER=$1
	PASS=$2
	echo "Checking user ${USER}..." 
	lftp -c "open ${HOST} -u ${USER},${PASS} ; ls" 
}

checkftpuserfolder $USER1 $PASS1
checkftpuserfolder $USER2 $PASS2
checkftpuserfolder $USER3 $PASS3

