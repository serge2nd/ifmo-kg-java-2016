@echo off
setlocal

rem if no args, call default execution
if [%1] == [] (
	set target=default
	goto exec
)

set arg1=%1

rem set "clean" and/or target
if [%2] == [] (
	if "%arg1%" == "clean" (
		set atFirst=clean
		goto exec
	)
	set target=%arg1%
	goto exec
)

set target=%2

if "%arg1%" == "clean" (
	set atFirst=clean
)

:exec
set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_112
if not [%target%] == [] set nextArgs=compiler:compile@%target%-compile process-resources jar:jar@%target%-jar
mvn %atFirst% %nextArgs%
