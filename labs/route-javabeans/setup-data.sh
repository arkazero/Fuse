#!/bin/bash

echo "Sending two test orders..."
curl -H "Content-Type: application/json" -X POST -d @/home/student/jb421/data/ordersJSON/order1.json http://localhost:8080/bookstore/rest/order/addOrder &> /dev/null
curl -H "Content-Type: application/json" -X POST -d @/home/student/jb421/data/ordersJSON/order2.json http://localhost:8080/bookstore/rest/order/addOrder &> /dev/null
echo "Orders sent!"
