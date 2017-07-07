#!/bin/bash
echo "Creating a batch of 1500 test inventory updates"
rm -rf items/incoming
mkdir -p items/incoming
cd items/incoming
for i in {1001..2500}
do
	cat > inventory-update-$(date "+%T.%6N")-$(( ( RANDOM % 5 ) + 1 )).csv << EOF
$i,$(( ( RANDOM % 15 ) + 1 ))
EOF
	sleep 0.001
done
