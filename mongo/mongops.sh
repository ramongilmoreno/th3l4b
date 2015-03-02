#!/bin/bash

#
# Lists mongodb daemon processes that can be left by JUnit testing if the test
# is halted abnormally before com.th3l4b.srm.sample.mongo.junit.MongoTest.after()
# (@After) is executed.
#
# Sample output of two spawned mongo daemons started by the
# com.th3l4b.srm.sample.mongo.junit.MongoTest JUnit test, but stopped during
# Eclipse debugging before end and clean up of the whole tests. This case
# shows that the test was debugged and stopped twice:
#
# $ ps auxww | grep mongod | grep -v grep
# 500      35188  0.4  0.8 335832 45296 ?        Sl   22:59   0:00 /tmp/extract-72c78839-1ebb-446a-a00e-2dcc2144f3f7mongod --dbpath /tmp/embedmongo-db-876f1e2f-5f18-4845-894a-3eac1cbedb3b --noauth --noprealloc --smallfiles --nojournal --nohttpinterface --port 37503 --syncdelay=0
# 500      35265  0.5  0.7 335832 43116 ?        Sl   22:59   0:00 /tmp/extract-550b98c5-398c-4f70-be04-1add98267daemongod --dbpath /tmp/embedmongo-db-06f1be1a-ae9d-43b9-ab9b-45f897855fea --noauth --noprealloc --smallfiles --nojournal --nohttpinterface --port 48879 --syncdelay=0
# $ 
#
ps auxww | grep mongod | grep -v grep | awk '{print $2}'
