package rmi;

import peer.Point;

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
    void splitZone(RemotePeerStub peer) throws RemoteException;
    Float calculateProximityTo(Point randomPoint)throws RemoteException;
    List<RemotePeerStub> findAvailableNodes() throws RemoteException;
    String desc() throws RemoteException;
}
