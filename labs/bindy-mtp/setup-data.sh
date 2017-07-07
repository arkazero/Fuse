#!/bin/bash

echo "Copying CSV file into items/incoming..."
rm -rf /home/student/jb421/labs/bindy-mtp/items/incoming/
mkdir -p /home/student/jb421/labs/bindy-mtp/items/incoming/
cp /home/student/jb421/data/catalogItems/items.csv /home/student/jb421/labs/bindy-mtp/items/incoming/
echo "Copy complete!"
