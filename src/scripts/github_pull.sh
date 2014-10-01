URL=https://raw.githubusercontent.com/ahmed-k/can/master/src/
#/bin/sh
rm *.java  peer/*.java rmi/*.java geometry/*.java driver/*.java debris/*.java
rm *.class peer/*.class rmi/*.class geometry/*.class driver/*.class debris/*.class
killall -v rmiregistry
curl $URL/rmi/RemotePeerStub.java > rmi/RemotePeerStub.java
curl $URL/rmi/RemoteLoggerStub.java > rmi/RemoteLoggerStub.java
curl $URL/rmi/Network.java > rmi/Network.java
curl $URL/geometry/Point.java > geometry/Point.java
curl $URL/geometry/CoordinateZone.java > geometry/CoordinateZone.java
curl $URL/geometry/Line.java > geometry/Line.java
curl $URL/peer/Peer.java > peer/Peer.java
curl $URL/peer/BootstrapPeer.java > peer/BootstrapPeer.java
curl $URL/driver/P2PDriver.java > driver/P2PDriver.java
curl $URL/driver/PeerLoader.java > driver/PeerLoader.java
curl $URL/debris/Command.java > debris/Command.java
curl $URL/debris/Constants.java > debris/Constants.java
curl $URL/debris/Logger.java > debris/Logger.java
javac *.java
javac peer/*.java
javac geometry/*.java
javac rmi/*.java
javac driver/*.java
javac debris/*.java
echo 'Successfully compiled project'