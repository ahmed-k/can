import rmi.RemotePeerStub;

import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
/**
 * Created by Ahmed Alabdullah on 9/23/14.
 */
public class Peer implements RemotePeerStub {


    private String name;
    public static Registry rmi;
    public static Scanner scanner;

    //public static String prefix = "compute-0-";


    public Peer(String name) {
        this.name = name;
    }

    public static Registry initRegistry() throws UnknownHostException, RemoteException {
        Registry retVal = null;
        String host = java.net.InetAddress.getLocalHost().toString();
        System.out.println("host is " + host);
        System.setProperty("java.rmi.server.hostname", host);
        retVal = LocateRegistry.createRegistry(1077);
        return retVal;
    }


    public static void main(String[] args) {

            if (Constants.BOOTSTRAP.equals(args[0])) {
                try {
                    rmi = initRegistry();
                    if (rmi == null) {
                        System.out.println("Could not initialize RMI registry...exiting...");
                        System.exit(1);
                    }
                    String host = java.net.InetAddress.getLocalHost().toString();
                    Peer first = new Peer(host);
                    RemotePeerStub _first = stub(first);
                    rmi.bind("bootstrap", _first);
                    System.out.println("Boostrap node bound, listening to nodes...");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        System.out.print(">>");
        String command = scanner.next();
        if ("join".equals(command)) {


        }



    }


    public static RemotePeerStub stub(Peer peer) throws RemoteException {
        return (RemotePeerStub) UnicastRemoteObject.exportObject(peer, 1077);
    }

    @Override
    public String sayHi() throws RemoteException {
        return name;
    }
}


