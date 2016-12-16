#!/bin/bash

# Check classes dir
classes_dir="$1"

if [[ "$classes_dir" == "" ]]; then
	echo "Specify classes dir"
	exit
fi

if [[ -f "$classes_dir" ]]; then
	echo "Cannot create $classes_dir"
	exit 1
fi

if [[ -d "$classes_dir" ]]; then
	rm -rf "$classes_dir"
fi

mkdir -p "$classes_dir"

# Compile
javac -cp lib/ImplementorTest.jar -d "$classes_dir" $(find src/main/java/ru/ifmo/ctddev/pistyulga/{common,implementor} -name *.java)

# JAR
rm -f *.jar
jar cmf META-INF/manifest-impler.mf kg-java-0.1-impler.jar\
	-C "$classes_dir" ru/ifmo/ctddev/pistyulga/common\
	-C src/main/java ru/ifmo/ctddev/pistyulga/common/log/logging.properties\
	-C "$classes_dir" ru/ifmo/ctddev/pistyulga/implementor

# Clean
rm -rf "$classes_dir"

