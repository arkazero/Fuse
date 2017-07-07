#!/bin/sh

mysql -ubookstore -predhat -h infrastructure bookstore -e "select * from order_ where discount is not null"

