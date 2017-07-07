#!/bin/sh

echo "Copying files for testing"
rm -rf /home/student/jb421/labs/enhancing-transaction/contact
mkdir -p /home/student/jb421/labs/enhancing-transaction/contact

cp -R /home/student/jb421/data/contact-transact/* /home/student/jb421/labs/enhancing-transaction/contact

echo "Files copied with success"
