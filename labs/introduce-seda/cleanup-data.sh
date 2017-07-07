#!/bin/bash
mysql --host=infrastructure.lab.example.com -ubookstore -predhat -t -e "delete from bookstore.OrderItem where id > 900"
mysql --host=infrastructure.lab.example.com -ubookstore -predhat -t -e "delete from bookstore.order_ where id > 900"
