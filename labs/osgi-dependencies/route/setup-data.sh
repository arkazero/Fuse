#!/bin/bash

echo 'Preparing incoming folder:'
echo '  Cleaning incoming folder...'
rm -rf /tmp/contact
mkdir -p /tmp/contact/incoming
echo '  Copying sample data files...'
cp ../../../data/contacts/* /tmp/contact/incoming
echo 'Preparation complete!'
