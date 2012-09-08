#!/bin/sh
#change root.path as you need
# then git update-index --assume-unchanged WebAlbums3/root.path
ROOT_PATH=$(cat root.path)
WA_HOME="."
java -Droot.path=$ROOT_PATH \
     -Dfile.encoding=UTF-8 \
	 -Djava.library.path=$WA_HOME/lib \
	 -jar $WA_HOME/dist/WebAlbums3-Bootstrap.jar
