package rmi;

import geometry.CoordinateZone;
import geometry.Point;

import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by Ahmed Alabdullah on 9/23/14.
 */
public interface RemotePeerStub extends Remote {


    Point pickRandomPoint() throws RemoteException;
    RemotePeerStub route(Point randomPoint) throws RemoteException, NotBoundException, UnknownHostException;
    Float calculateProximityTo(Point randomPoint)throws RemoteException;
    List<RemotePeerStub> findAvailableNodes() throws RemoteException;
    String desc() throws RemoteException;
    void accomodate(RemotePeerStub peer, Point peerPoint) throws RemoteException;
    void accept(CoordinateZone newZone, List<RemotePeerStub> departingNeighbors) throws RemoteException;

    boolean doesntTouch(CoordinateZone zone) throws RemoteException;
    void welcomeNewNeighbor(RemotePeerStub neighbor) throws RemoteException;
    void notifyDeparture(RemotePeerStub neighbor) throws RemoteException;

    void insert(Point insertionPoint, String keyword) throws RemoteException, NotBoundException, UnknownHostException;

    String ip() throws RemoteException;

    void search(Point insertionPoint, String keyword) throws RemoteException, NotBoundException, UnknownHostException;

    void addOnlineNode(RemotePeerStub peer) throws RemoteException;

    void leave() throws RemoteException, NotBoundException, UnknownHostException;

    Float zoneSize() throws RemoteException;

    boolean willMergeUniformly(CoordinateZone zone) throws RemoteException;

    void own(CoordinateZone zone, java.util.Map hashtable, List<RemotePeerStub> neighbors) throws RemoteException;

    void removeOnlineNode(RemotePeerStub peer) throws RemoteException;

    String info() throws RemoteException;

    void setLogger(RemoteLoggerStub logger);
}
