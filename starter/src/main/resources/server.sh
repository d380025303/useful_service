#!/bin/bash
port=5000
kill -9 $(netstat -tunlp | grep $port | grep -o '[0-9]\+/java' | grep -o '[0-9]\+')

APP_NAME='useful_service'
JAR_NAME='starter-1.0-SNAPSHOT.jar'

current_time=`date +"%Y-%m-%d_%H_%M_%S"`
JAVA_OPTS="$JAVA_OPTS -server -Xms2048m -Xmx2048m -XX:MetaspaceSize=32m -XX:MaxMetaspaceSize=512m"
JAVA_OPTS="$JAVA_OPTS -XX:+HeapDumpOnOutOfMemoryError"
JAVA_OPTS="$JAVA_OPTS -Dfile.encoding=UTF-8"
JAVA_OPTS="$JAVA_OPTS -Duser.timezone=Etc/GMT-8"
JAVA_OPTS="$JAVA_OPTS -Dsun.net.client.defaultConnectTimeout=3000"
JAVA_OPTS="$JAVA_OPTS -Dspring.config.location=config/"
JAVA_OPTS="$JAVA_OPTS -Dlogging.config=config/logback-spring.xml"
JAVA_OPTS="$JAVA_OPTS -verbose:gc -Xloggc:logs/gc-"$current_time".log -XX:+PrintGCDetails "

CURRENT_DIR=`pwd`
if [ ! -f "$CURRENT_DIR/$JAR_NAME" ]; then
	cd ..
fi
CURRENT_DIR=`pwd`

_EXECJAVA=java
if [ -f "jdk/bin/java" ]; then
	_EXECJAVA="jdk/bin/java"
elif [ -f "jre/bin/java" ]; then
	_EXECJAVA="jre/bin/java"
elif [ $JAVA_8_HOME ]; then
    _EXECJAVA="$JAVA_8_HOME/bin/java"
fi

echo "$APP_NAME is running..."
nohup $_EXECJAVA $JAVA_OPTS -jar $CURRENT_DIR/$JAR_NAME > /dev/null 2>&1 &
