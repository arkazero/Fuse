#!/bin/bash

rm -fr orders
mkdir -p orders/incoming

cp -a ~/jb421/data/orders/* orders/incoming
