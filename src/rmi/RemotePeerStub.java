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
    void accomodate(RemotePeerStub peer) throws RemoteException;
    void accept(CoordinateZone newZone, List<RemotePeerStub> departingNeighbors) throws RemoteException;

    boolean noLongerANeighbor(CoordinateZone zone) throws RemoteException;
    void welcomeNewNeighbor(RemotePeerStub neighbor) throws RemoteException;
    void notifyDeparture(RemotePeerStub neighbor) throws RemoteException;
}
