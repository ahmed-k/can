#/bin/sh
cd ..
rm *.class peer/*.class rmi/*.class geometry/*.class driver/*.class debris/*.class
killall -v rmiregistry
javac *.java
javac peer/*.java
javac geometry/*.java
javac rmi/*.java
javac driver/*.java
javac debris/*.java
echo 'Successfully compiled project'