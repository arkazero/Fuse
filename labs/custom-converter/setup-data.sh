#!/bin/bash

echo "Copying CSV file into orders/incoming..."
rm -rf /home/student/jb421/labs/custom-converter/orders/incoming/
mkdir -p /home/student/jb421/labs/custom-converter/orders/incoming/
cp /home/student/jb421/data/orderCSV/orders.csv /home/student/jb421/labs/custom-converter/orders/incoming/
echo "Copy complete!"
