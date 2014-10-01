CS657
Assignment #1 : Peer To Peer Systems Architecture
Ahmed Alabdullah
======
1. OVERVIEW (LANGUAGE, DESIGN DECISIONS)
2. PACKAGE STRUCTURE
3. EXECUTION INSTRUCTIONS

1. Overview
===========
This is my submission of the requirements necessary to complete Assignment 1. After an initial toying around with Go, I decided to finish the assignment
in JAVA, utilizing Remote Method Invocation (RMI).

This program was tested on a MEDUSA cluster.
Nodes are addressable by the prefix compute-0-X where X is the node number (1 through 20, although 20 could not be reached in my tests).
The bootstrap node is compute-0-1, it will automatically join the system when it is loaded.
The program should be able to handle the four main features required by the assignment. A centralized logger resides on the bootstrap node and receives logs from all
the distributed parts of the system which is then output to the P2PDriver used to interface with the system.
A constraint I imposed is that the bootstrap node needs to be online at all times, time constraints prevented me from implementing a mechanism which transfers the
bootstrapping to another active node. However, when a node join the bootstrap node will connect it with any random online node, which actually bootstraps the new node.
Please do not have the boostrapping node (compute-0-1) leave the system.







2. Package Structure
========================
The structure of the package is as follows:
1. ahmed_alabdullah_CS657_assignment1.tar       <= compressed tarball file
2. README.txt <= this file

uncompress the tarball by the following command on MEDUSA:

tar xvzf ahmed_alabdullah_CS657_assignment1.tar

the directory structure extracted from the tarball is as follows:

debris/
driver/
geometry/
peer/
rmi/
test/
scripts/

DEBRIS: contains Constants used in the program, as a well a Logger class which logs all messages incoming from the distributed nodes in the system to be output at
the interfacing terminal.
DRIVER: contains two java classes, PeerLoader and P2PDriver. PeerLoader is used to initially start up the nodes as daemon processes on every medusa node.
the P2PDriver can be run from any medusa node to interface with the system with the commands required by the assignment. Scripts are provided to make this
process easier.
GEOMETRY: These are geometrical classes used by the system to handle all the mathematical aspects related to the system( to calculate adjacency,
 if a point is part of a node, etc...)
RMI: Two remote interfaces for objects needed to be exposed on the network (Logger and Peer), as well as a Network class serving as a service to talk to the
network infrastructure by the application.
PEER: Those are the actual peer classes. The BootstrapPeer has additional state and methods only needed to be completed by a Bootstrapping Peer.
SCRIPTS: bash scripts written to facilitate execution.
connectPeer.sh : starts a background process on a node which connects a peer to the network.
A connected peer has not yet joined the actual P2P system but it is online and reachable.

github_pull.sh: I used this to transfer my files from my local machine to MEDUSA using github. running this will pull a fresh copy from github and compile the application.
compile.sh: erases all compiled classes and compiles the application again.
run.sh : can be executed on any machine to start up a small command-line terminal interface to the system which accepts system commands.

TEST: small JUnit tests I wrote to help me fix bugs. Included for completion.


3. EXECUTION:
============
Inside script, run compile.sh and then run.sh
in cases of failure and rerunning after that, it might be required to execute run.sh twice as the port will take time to be shut down even after the java process is killed,
and you will get a Port already in use exception on the first run.sh, so running it a second time solves it.






