@echo off
setlocal

set CLASSPATH=%JAVA_HOME%\..\lib\tools.jar;lib\*;target\kg-java-0.1-common.jar

set WALK_MAIN=ru.ifmo.ctddev.pistyulga.walk.WalkMain
set ARRAYSET=ru.ifmo.ctddev.pistyulga.arrayset.util.ArraySet
set IMPLER=ru.ifmo.ctddev.pistyulga.implementor.Implementor
set IP=ru.ifmo.ctddev.pistyulga.concurrent.PMIterativeParallelism
set PM=ru.ifmo.ctddev.pistyulga.concurrent.ParallelMapperImpl

set WALK_TESTER=info.kgeorgiy.java.advanced.walk.Tester
set ARRAYSET_TESTER=info.kgeorgiy.java.advanced.arrayset.Tester
set IMPLER_TESTER=info.kgeorgiy.java.advanced.implementor.Tester
set IP_TESTER=info.kgeorgiy.java.advanced.concurrent.Tester
set PM_TESTER=info.kgeorgiy.java.advanced.mapper.Tester

java -cp "%CLASSPATH%;target\kg-java-0.1-walk.jar" %WALK_TESTER% RecursiveWalk %WALK_MAIN%
java -cp "%CLASSPATH%;target\kg-java-0.1-arrayset.jar" %ARRAYSET_TESTER% NavigableSet %ARRAYSET%
java -cp "%CLASSPATH%;target\kg-java-0.1-impler.jar" %IMPLER_TESTER% class %IMPLER%
java -Dfile.encoding="UTF-16" -cp "%CLASSPATH%;target\kg-java-0.1-impler.jar" %IMPLER_TESTER% jar-class %IMPLER%
java -cp "%CLASSPATH%;target\kg-java-0.1-mapper.jar" %IP_TESTER% list %IP%
java -cp "%CLASSPATH%;target\kg-java-0.1-mapper.jar" %PM_TESTER% list %PM%,%IP%
