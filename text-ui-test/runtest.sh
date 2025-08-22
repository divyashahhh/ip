#!/usr/bin/env bash

# create bin directory if it doesn't exist
[ -d "../bin" ] || mkdir ../bin

# delete output from previous run
[ -e "ACTUAL.TXT" ] && rm ACTUAL.TXT

# compile ALL .java files recursively from src/main/java
if ! javac -cp ../src/main/java -Xlint:none -d ../bin $(find ../src/main/java -name "*.java"); then
  echo "********** BUILD FAILURE **********"
  exit 1
fi

# run the program, feed commands from input.txt, capture output
if ! java -classpath ../bin cs2103.Paneer < input.txt > ACTUAL.TXT; then
  echo "********** RUN FAILURE **********"
  exit 1
fi

# compare the output to the expected output
diff ACTUAL.TXT EXPECTED.TXT
if [ $? -eq 0 ]; then
  echo "Test result: PASSED"
  exit 0
else
  echo "Test result: FAILED"
  exit 1
fi