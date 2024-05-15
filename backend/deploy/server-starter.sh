#!/bin/bash
if [ $# -ne 1 ]
then
        echo "arguments error!"
        exit 1
else
        jarname=$1

        echo "Running Server --->$jarname"$
        nohup java -jar "./$jarname" >server-log.log 2>&1 &
fi
