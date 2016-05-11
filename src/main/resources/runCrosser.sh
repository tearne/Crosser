#! /bin/bash
java -cp Crosser-*.jar -Dlogback.configurationFile=./logback.xml org.tearne.Crosser $1
