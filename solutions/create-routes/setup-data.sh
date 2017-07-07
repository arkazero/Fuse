#!/bin/bash
echo 'Preparing test folder:'
echo '  Cleaning test folder...'
rm -rf /tmp/orders
mkdir -p /tmp/orders/incoming
echo '  Copying sample data files...'
cp ../../data/testOrders/* /tmp/orders/incoming
echo 'Preparation complete!'
