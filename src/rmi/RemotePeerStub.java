package rmi;

import geometry.CoordinateZone;
import geometry.Point;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by Ahmed Alabdullah on 9/23/14.
 */
public interface RemotePeerStub extends Remote {


    public String sayHi() throws RemoteException;
    public void send(String msg) throws RemoteException;

    Point pickRandomPoint() throws RemoteException;
    RemotePeerStub route(Point randomPoint) throws RemoteException;
    Float calculateProximityTo(Point randomPoint)throws RemoteException;
    List<RemotePeerStub> findAvailableNodes() throws RemoteException;
    String desc() throws RemoteException;
    void accomodate(RemotePeerStub peer, Point peerPoint) throws RemoteException;
    void accept(CoordinateZone newZone, List<RemotePeerStub> departingNeighbors) throws RemoteException;

    boolean noLongerANeighbor(CoordinateZone zone) throws RemoteException;
    void welcomeNewNeighbor(RemotePeerStub neighbor) throws RemoteException;
    void notifyDeparture(RemotePeerStub neighbor) throws RemoteException;

    void insert(Point insertionPoint, String keyword) throws RemoteException;

    String ip() throws RemoteException;

    void search(Point insertionPoint, String keyword) throws RemoteException;

    void addOnlineNode(RemotePeerStub peer) throws RemoteException;

    String leave() throws RemoteException;

    Float zoneSize() throws RemoteException;

    boolean willMergeUniformly(CoordinateZone zone) throws RemoteException;

    void own(CoordinateZone zone, java.util.Map hashtable) throws RemoteException;
}
