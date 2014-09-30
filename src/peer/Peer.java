package peer;

import debris.Logger;
import geometry.CoordinateZone;
import geometry.Point;
import rmi.RemotePeerStub;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * Created by Ahmed Alabdullah on 9/23/14.
 */
public class Peer implements RemotePeerStub {


    protected CoordinateZone zone;
    protected String name;
    protected String ip;
    protected RemotePeerStub stub;
    protected List<RemotePeerStub> neighbors = new ArrayList<RemotePeerStub>();
    protected Map<String, String> hashtable = new Hashtable<String, String>();

    public Peer(String name, String ip) {

        this.name = name;
        this.ip = ip;
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


    public CoordinateZone getZone() {
        return zone;
    }

    public void setZone(CoordinateZone zone) {
        this.zone = zone;
    }

    public static RemotePeerStub stub(Peer peer) throws RemoteException {
        return (RemotePeerStub) UnicastRemoteObject.exportObject(peer, 1077);
    }

    public Point pickRandomPoint() throws RemoteException {
        Random r = new Random();
        float x = r.nextInt(10);
        float y = r.nextInt(10);
        return new Point(x, y);

    }

    @Override
    public RemotePeerStub route(Point randomPoint) throws RemoteException {
        if (zone.hasPoint(randomPoint)) {
            System.out.println(name + " has the point!");
            return stub;
        } else {
            return routeToClosestNeighbor(randomPoint);
        }

    }

    @Override
    public Float calculateProximityTo(Point randomPoint) {
        return zone.proximityTo(randomPoint);
    }

    @Override
    public List<RemotePeerStub> findAvailableNodes() throws RemoteException {
        return null;
    }

    @Override
    public String desc() throws RemoteException {
        return name + " : " + zone.toString();
    }

    @Override
    public void accomodate(RemotePeerStub peer, Point peerPoint) throws RemoteException {
        CoordinateZone newZone = null;
        if (zone.inASubZone(peerPoint)) {
            newZone = zone.getSubzoneOwning(peerPoint);
        } else {
            newZone = zone.splitInHalf();
        }
        System.out.println("zone split and now has dimensions: " + zone);
        System.out.println("new zone has dimensions" + newZone);
        List<RemotePeerStub> newZoneNeighbors = addNeighborsToNewPeer(zone, newZone);
        this.welcomeNewNeighbor(peer);
        peer.accept(newZone, newZoneNeighbors);
        System.out.println("node " + name + " has neighbors: " + neighbors());


    }


    private List<RemotePeerStub> addNeighborsTouching(CoordinateZone newZone) throws RemoteException {
        List<RemotePeerStub> retVal = new ArrayList<RemotePeerStub>();
        for (RemotePeerStub neighbor: neighbors) {
            if (!neighbor.doesntTouch(zone)) {
                System.out.println(neighbor.desc() + " still a neighbor of " + desc() + " but also will be neighbor of new node");
                retVal.add(neighbor);
            }

        }
        return retVal;
    }

    private List<RemotePeerStub> addNeighborsToNewPeer(CoordinateZone oldZone, CoordinateZone newZone) throws RemoteException {
        List<RemotePeerStub> neighbors = new ArrayList<RemotePeerStub>();
        neighbors.addAll(removeDepartingNeighborsNotTouching(oldZone));
        neighbors.addAll(addNeighborsTouching(newZone));
        neighbors.add(stub);
        return neighbors;

    }

    private String neighbors() throws RemoteException {
        String retVal = "";
        for (RemotePeerStub neighbor : neighbors) {
            retVal += neighbor.desc() + " ";
        }
        retVal += ".";
        return retVal;
    }

    private List<RemotePeerStub> removeDepartingNeighborsNotTouching(CoordinateZone zone) throws RemoteException {
        List<RemotePeerStub> retVal = new ArrayList<RemotePeerStub>();
        for (RemotePeerStub neighbor : neighbors) {
            if (neighbor.doesntTouch(zone)) {
                System.out.println("Neighbor " + neighbor.desc() + " no longer a neighbor!");
                neighbor.notifyDeparture(stub);
                retVal.add(neighbor);
            }
        }
        return retVal;
    }

    @Override
    public void accept(CoordinateZone newZone, List<RemotePeerStub> newNeighbors) throws RemoteException {
        zone = newZone;
        neighbors = newNeighbors;
        System.out.println("accepted new zone " + zone);

        for (RemotePeerStub neighbor : neighbors) {
            neighbor.welcomeNewNeighbor(stub);

        }
        System.out.println("node " + name + " has neighbors: " + neighbors());
    }

    @Override
    public boolean doesntTouch(CoordinateZone zone) throws RemoteException {
        return this.zone.notAdjacentTo(zone);
    }

    @Override
    public void welcomeNewNeighbor(RemotePeerStub neighbor) throws RemoteException {
        if (!neighbors.contains(neighbor)) {
            neighbors.add(neighbor);
        }

    }

    @Override
    public void notifyDeparture(RemotePeerStub neighbor) throws RemoteException {
        neighbors.remove(neighbor);
    }

    @Override
    public void insert(Point insertionPoint, String keyword) throws RemoteException {
        if (zone.hasPoint(insertionPoint)) {
            System.out.println("keyword " + keyword + " stored in " + name);
            hashtable.put(keyword, "file represented by " + keyword);
        } else {
            RemotePeerStub closest = routeToClosestNeighbor(insertionPoint);
            System.out.println(" Routed to peer " + closest.desc() + "with IP address" + closest.ip());
            closest.insert(insertionPoint, keyword);
        }

    }

    @Override
    public String ip() throws RemoteException {
        return ip;
    }

    @Override
    public void search(Point insertionPoint, String keyword) throws RemoteException {
        if (zone.hasPoint(insertionPoint)) {
            System.out.println("Found at peer " + name + " with IP " + ip);
        } else {
            RemotePeerStub closest = routeToClosestNeighbor(insertionPoint);
            System.out.println(" Routed to peer " + closest.desc() + "with IP address" + closest.ip());
            closest.search(insertionPoint, keyword);
        }
    }

    @Override
    public void addOnlineNode(RemotePeerStub peer) throws RemoteException {

    }

    @Override
    public String leave() throws RemoteException {
        SortedMap<Float, RemotePeerStub> size = new TreeMap<Float, RemotePeerStub>();
        for (RemotePeerStub neighbor : neighbors) {
            if (neighbor.willMergeUniformly(zone)) {
                merge(neighbor);
                Logger.log("Successfully merged with" + neighbor.desc());
                return Logger.deliverLog();
            } else {
                size.put(neighbor.zoneSize(), neighbor);
            }
        }
        RemotePeerStub smallest = size.get(size.firstKey());
        merge(smallest);
        Logger.log("Successfully merged with" + smallest.desc());
        return Logger.deliverLog();


    }

    private void merge(RemotePeerStub neighbor) throws RemoteException {
        neighbor.own(zone, hashtable, neighbors);
        for (RemotePeerStub n: neighbors) {
            n.notifyDeparture(stub);
        }
        zone = null;
        neighbors.clear();
        hashtable.clear();

    }


    public Float zoneSize() throws RemoteException {
        return zone.size();
    }

    public boolean willMergeUniformly(CoordinateZone zone) throws RemoteException {
        return this.zone.willMergeUniformly(zone);

    }

    @Override
    public void own(CoordinateZone zone, Map hashtable, List<RemotePeerStub> neighbors) throws RemoteException {
        this.zone.merge(zone);
        this.hashtable.putAll(hashtable);
        mergeNeighbors(neighbors);
    }

    private void mergeNeighbors(List<RemotePeerStub> newNeighbors) throws RemoteException {
        for (RemotePeerStub newNeighbor: newNeighbors) {
            if (!neighbors.contains(newNeighbor) && ( !(newNeighbor.ip().equals( this.ip() ) ) ) ) {
                neighbors.add(newNeighbor);
            }
        }
    }

    @Override
    public void removeOnlineNode(RemotePeerStub peer) throws RemoteException {

    }

    private RemotePeerStub routeToClosestNeighbor(Point randomPoint) throws RemoteException {
        SortedMap<Float, RemotePeerStub> proximity = new TreeMap<Float, RemotePeerStub>();
        for (RemotePeerStub neighbor : neighbors) {
            proximity.put(neighbor.calculateProximityTo(randomPoint), neighbor);
        }
        RemotePeerStub closestNeighbor = proximity.get(proximity.firstKey());
        return closestNeighbor.route(randomPoint);
    }

    private String data() {
        String retVal = "";
        for (Map.Entry<String, String> e : hashtable.entrySet()) {
            retVal += e.getKey() + " => " + e.getValue() + "\n";
        }
        return retVal;

    }

    public void setStub(RemotePeerStub stub) {
        this.stub = stub;
    }

    public String info() {
        try {
            return "Peer name: " + name + "\n" +
                    "IP Address: " + ip + "\n" +
                    "Zone Coordinate: " + zone + "\n" +
                    "Neighbors: " + neighbors() + "\n" +
                    "Data: " + data();
        } catch (RemoteException e) {
            return "FAILURE";
        }

    }
}


