#!/bin/bash
echo "Creating a batch of 5 test orders with 300 order items each"
mysql --host=infrastructure.lab.example.com -ubookstore -predhat -t -e "delete from bookstore.OrderItem where id > 900"
mysql --host=infrastructure.lab.example.com -ubookstore -predhat -t -e "delete from bookstore.order_ where id > 900"
mysql --host=infrastructure.lab.example.com -ubookstore -predhat -t -e "delete from bookstore.Customer where id = 35"
mysql --host=infrastructure.lab.example.com -ubookstore -predhat -t -e"INSERT INTO bookstore.Customer VALUES (35,0,'jdoe@doe.com','John','Doe','redhat','jdoe',NULL,NULL),(36,0,'guest@doe.com','Guest','User','user','guest',NULL,NULL),(37,1,'admin@bookshop.com','Admin','User','redhat','admin',NULL,NULL) ON DUPLICATE KEY UPDATE id=id;"
for i in {1000..1004}
do
	mysql --host=infrastructure.lab.example.com -ubookstore -predhat -t -e "insert into bookstore.order_ (id,delivered,cust_id) values ($i,0, $(( ( RANDOM % 3 ) + 35 ))) ON DUPLICATE KEY UPDATE delivered=0;"
	for j in {1..300}
	do
		mysql --host=infrastructure.lab.example.com -ubookstore -predhat -t -e "insert into bookstore.OrderItem (id,order_id,item_id, quantity) values ($i$j,$i,$(( ( RANDOM % 32 ) + 1 )),$(( ( RANDOM % 5 ) + 1 ))) ON DUPLICATE KEY UPDATE id=id;"
	done

	echo "Order $(($i - 999)) was created!"
done
