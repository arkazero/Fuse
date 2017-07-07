#!/bin/bash

echo "Sending two test orders..."
curl -H "Content-Type: application/json" -X POST -d @sample.json http://localhost:8080/bookstore/rest/order/addOrder
curl -H "Content-Type: application/json" -X POST -d @sample.json http://localhost:8080/bookstore/rest/order/addOrder
echo "Orders sent!"
