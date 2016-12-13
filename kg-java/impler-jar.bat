@echo off
setlocal enabledelayedexpansion

rem Check destination classes directory
set classes_dir=%1

if [%classes_dir%] == [] (
	echo Specify classes dir
	exit /b 0
)

if not exist %classes_dir%\* (
	if exist %classes_dir% (
		echo Cannot create dir %classes_dir%
		exit /b 1
	)
) else (
	rd /q /s %classes_dir%
)

md %classes_dir%

rem Build list of source files
for /f %%i in ('where /r src\main\java\ru\ifmo\ctddev\pistyulga\common\ *.java') do (
	set src_files=!src_files! %%i
)
for /f %%i in ('where /r src\main\java\ru\ifmo\ctddev\pistyulga\implementor\ *.java') do (
	set src_files=!src_files! %%i
)

rem Compile
javac -cp lib\ImplementorTest.jar -d %classes_dir% %src_files%

rem JAR
del /q *.jar
jar cmf META-INF\manifest-impler.mf kg-java-0.1-impler.jar^
 -C %classes_dir% ru\ifmo\ctddev\pistyulga\common^
 -C src\main\java ru\ifmo\ctddev\pistyulga\common\log\logging.properties^
 -C %classes_dir% ru\ifmo\ctddev\pistyulga\implementor
rem jar cmf META-INF\manifest-common.mf kg-java-0.1-common.jar -C %classes_dir% ru\ifmo\ctddev\pistyulga\common^
rem -C src\main\java ru\ifmo\ctddev\pistyulga\common\log\logging.properties

rem Clean
rd /q /s %classes_dir%
