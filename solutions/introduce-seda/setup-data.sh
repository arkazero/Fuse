#!/bin/bash
mysql --host=infrastructure.lab.example.com -ubookstore -predhat -t -e "delete from bookstore.OrderItem where id > 0;"
mysql --host=infrastructure.lab.example.com -ubookstore -predhat -t -e "delete from bookstore.order_ where id >= 1000;"
echo "Creating a batch of 5 test orders with 300 order items each"
for i in {1000..1004}
do
	mysql --host=infrastructure.lab.example.com -ubookstore -predhat -t -e "insert into bookstore.order_ (id,delivered) values ($i,0);"
	for j in {1..300}
	do
		mysql --host=infrastructure.lab.example.com -ubookstore -predhat -t -e "insert into bookstore.OrderItem (id,order_id,item_id, quantity) values ($i$j,$i,$(( ( RANDOM % 32 ) + 1 )),$(( ( RANDOM % 5 ) + 1 )))"
	done

	echo "Order $(($i - 999)) was created!"
done
