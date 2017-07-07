#!/bin/bash

echo "Sending two test orders..."
curl -H "Content-Type: application/json" -X POST -d @sample.json http://localhost:8080/bookstore/rest/order/addOrder &> /dev/null
curl -H "Content-Type: application/json" -X POST -d @sample.json http://localhost:8080/bookstore/rest/order/addOrder &> /dev/null
echo "Orders sent!"
