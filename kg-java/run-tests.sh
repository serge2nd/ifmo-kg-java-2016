#!/bin/bash

SEP=";"

CLASSPATH="$JAVA_HOME/../lib/tools.jar${SEP}lib/*${SEP}target/kg-java-0.1-common.jar"

WALK_MAIN="ru.ifmo.ctddev.pistyulga.walk.WalkMain"
ARRAYSET="ru.ifmo.ctddev.pistyulga.arrayset.util.ArraySet"
IMPLER="ru.ifmo.ctddev.pistyulga.implementor.Implementor"
IP="ru.ifmo.ctddev.pistyulga.concurrent.PMIterativeParallelism"
PM="ru.ifmo.ctddev.pistyulga.concurrent.ParallelMapperImpl"

WALK_TESTER="info.kgeorgiy.java.advanced.walk.Tester"
ARRAYSET_TESTER="info.kgeorgiy.java.advanced.arrayset.Tester"
IMPLER_TESTER="info.kgeorgiy.java.advanced.implementor.Tester"
IP_TESTER="info.kgeorgiy.java.advanced.concurrent.Tester"
PM_TESTER="info.kgeorgiy.java.advanced.mapper.Tester"

java -cp "$CLASSPATH${SEP}target/kg-java-0.1-walk.jar" $WALK_TESTER RecursiveWalk $WALK_MAIN
java -cp "$CLASSPATH${SEP}target/kg-java-0.1-arrayset.jar" $ARRAYSET_TESTER NavigableSet $ARRAYSET
java -cp "$CLASSPATH${SEP}target/kg-java-0.1-impler.jar" $IMPLER_TESTER class $IMPLER
java -Dfile.encoding="UTF-16" -cp "$CLASSPATH${SEP}target/kg-java-0.1-impler.jar" $IMPLER_TESTER jar-class $IMPLER
java -cp "$CLASSPATH${SEP}target/kg-java-0.1-mapper.jar" $IP_TESTER list $IP
java -cp "$CLASSPATH${SEP}target/kg-java-0.1-mapper.jar" $PM_TESTER list $PM,$IP