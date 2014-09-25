package peer;

import rmi.RemotePeerStub;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import static debris.Constants.BOOTSTRAP;
import static debris.Constants.UNIVERSE;

/**
 * Created by Ahmed Alabdullah on 9/23/14.
 */
public class Peer implements RemotePeerStub {


    private CoordinateZone zone;
    private String name;
    private RemotePeerStub stub;
    private List<RemotePeerStub> neighbors = new ArrayList<RemotePeerStub>();
    private List<RemotePeerStub> onlineNodes = new ArrayList<RemotePeerStub>();

    public Peer(String name) {
        if(BOOTSTRAP.equals(name)) {
            this.setZone(new CoordinateZone(new Point(0,0), new Point(0,UNIVERSE), new Point(UNIVERSE,0), new Point(UNIVERSE,UNIVERSE)));

            try {
                stub = stub(this);
                onlineNodes.add(stub);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        this.name = name;
    }

    public Peer(String name, CoordinateZone zone) {
        this.name = name;
        this.zone = zone;
    }

    @Override
    public String sayHi() throws RemoteException {
        return name;
    }

    @Override
    public void send(String msg) {
        System.out.println(msg);
    }

    @Override
    public List<RemotePeerStub> findAvailableNodes() {
        return onlineNodes;
    }

    public CoordinateZone getZone() {
        return zone;
    }

    public void setZone(CoordinateZone zone) {
        this.zone = zone;
    }

    public static RemotePeerStub stub(Peer peer) throws RemoteException {
        return (RemotePeerStub) UnicastRemoteObject.exportObject(peer, 1077);
    }

    public Point pickRandomPoint() throws RemoteException{
        Random r = new Random();
        float x = r.nextInt(10);
        float y = r.nextInt(10);
        return new Point(x,y);

    }

    @Override
    public RemotePeerStub route(Point randomPoint) throws RemoteException {
        if ( zone.hasPoint(randomPoint) ) {
            return stub;
        }
        else {
            return routeToClosestNeighbor(randomPoint);
        }

    }

    @Override
    public Float calculateProximityTo(Point randomPoint) {
        return zone.distanceFromCenterTo(randomPoint);
    }


    @Override
    public void splitZone(RemotePeerStub peer) throws RemoteException {

    }

    private RemotePeerStub routeToClosestNeighbor(Point randomPoint) throws RemoteException {
        Map<Float, RemotePeerStub> proximity = new TreeMap<Float, RemotePeerStub>();
        for (RemotePeerStub neighbor : neighbors) {
            proximity.put(neighbor.calculateProximityTo(randomPoint), neighbor);
        }
        RemotePeerStub closestNeighbor = proximity.get(0);
        return closestNeighbor.route(randomPoint);
    }



}


