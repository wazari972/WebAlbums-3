#! /bin/sh

TOMME_XML=$1

USERNAME=wazari972
PASSWORD=ijaheb
ADDRESS=127.0.0.1
DB_NAME=WebAlbums_TEST

sed -i "s/USERNAME/$USERNAME/g" $TOMME_XML
sed -i "s/PASSWORD/$PASSWORD/g" $TOMME_XML
sed -i "s/ADDRESS/$ADDRESS/g" $TOMME_XML
sed -i "s/DB_NAME/$DB_NAME/g" $TOMME_XML