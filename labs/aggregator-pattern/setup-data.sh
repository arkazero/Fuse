#!/bin/bash

echo "Copying fixed length file into orders/incoming..."
rm -rf  /home/student/jb421/labs/aggregator-pattern/orders/incoming/
mkdir -p /home/student/jb421/labs/aggregator-pattern/orders/incoming/
cp /home/student/jb421/data/ordersFixedLength/orders.txt /home/student/jb421/labs/aggregator-pattern/orders/incoming/
echo "Copy complete!"
