#!/bin/bash -e

rm -f core* hs_err* *.o *.so *.class example_wrap.cxx example_wrap.h
swig4 -java -c++ -package example -outdir example example.i
g++ -I/System/Library/Frameworks/JavaVM.framework/Versions/Current/Headers/ -g -fPIC -c example_wrap.cxx
g++ -I/System/Library/Frameworks/JavaVM.framework/Versions/Current/Headers/ -g -fPIC -c example.cpp
g++ -shared example_wrap.o example.o -o libexample.dylib
javac example/*.java
javac Triangle.java
javac example_test.java
java -cp . example_test


