#!/bin/bash
if [ $# -ne 1 ]
then
        echo "arguments error!"
        exit 1
else
        port=$1

        pid=$(netstat -nlp | grep :$port | awk '{print $7}' | awk -F"/" '{print $1}')
        if [ -n "$pid" ]; then
          echo "server is running in: $pid"
          echo "Killing Server At port--->$port"
          kill -9 $pid
        fi

fi
