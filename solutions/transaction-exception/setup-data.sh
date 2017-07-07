#!/bin/sh


echo "Creating data directory"
mkdir -p /home/student/jb421/labs/transaction-exception/orders

echo "Copying files to for testing"
cp -R /home/student/jb421/data/orders-transact/* /home/student/jb421/labs/transaction-exception/orders

echo "Cleaning database"
mysql -ubookstore -predhat -h infrastructure bookstore -e "delete from OrderItem" &>/dev/null
mysql -ubookstore -predhat -h infrastructure bookstore -e "delete from order_" &>/dev/null

echo "Files copied with success"
