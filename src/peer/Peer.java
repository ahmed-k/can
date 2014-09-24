package peer;

import rmi.RemotePeerStub;

import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
/**
 * Created by Ahmed Alabdullah on 9/23/14.
 */
public class Peer implements RemotePeerStub {

    public static String bootstrapNode = "compute-0-1";
    private String name;
    public static Registry rmi;
    public static Scanner scanner = new Scanner(System.in);

    //public static String prefix = "compute-0-";


    public Peer(String name) {
        this.name = name;
    }

    public static Registry initRegistry() throws UnknownHostException, RemoteException, AlreadyBoundException {
        Registry retVal = null;
        String host = java.net.InetAddress.getLocalHost().toString().split(".local")[0];
        System.out.println("host is " + host);
        System.setProperty("java.rmi.server.hostname", host);

                if (bootstrapNode.equals(host)) {
                    retVal = LocateRegistry.createRegistry(1077);
                    Peer first = new Peer(host);
                    RemotePeerStub _first = stub(first);
                    retVal.bind(Constants.BOOTSTRAP, _first);

                    System.out.println("Boostrap node bound, listening to nodes...");
                }
                else {
                    retVal = LocateRegistry.getRegistry(bootstrapNode,1077);
                    System.out.println("Connected " + host + " to network...");
                }

        return retVal;
    }


    public static void main(String[] args) {

                try {
                    rmi = initRegistry();
                    if (rmi == null) {
                        System.out.println("Could not initialize RMI registry...exiting...");
                        System.exit(1);
                    }

                    RemotePeerStub test = find(Constants.BOOTSTRAP);
                    System.out.println(test.sayHi());


                } catch (Exception e) {
                    e.printStackTrace();
                }





    }

    public static RemotePeerStub find(String node) throws RemoteException, NotBoundException {
        return (RemotePeerStub) rmi.lookup(node);
    }

    public static RemotePeerStub stub(Peer peer) throws RemoteException {
        return (RemotePeerStub) UnicastRemoteObject.exportObject(peer, 1077);
    }

    @Override
    public String sayHi() throws RemoteException {
        return name;
    }
}


