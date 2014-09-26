package rmi;

import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by Ahmed Alabdullah on 9/25/14.
 */
public class Network {



    public static Registry getRegistry() throws RemoteException, UnknownHostException {

        return LocateRegistry.getRegistry(Network.getHost(), 1077);

    }

    public static Registry getRegistry(String host) throws RemoteException, UnknownHostException {

        return LocateRegistry.getRegistry(host, 1077);
    }

    public static String getHost() throws UnknownHostException {
        return java.net.InetAddress.getLocalHost().toString().split(".local/")[0];
    }

    public static String getIPAddress() throws UnknownHostException {
        return java.net.InetAddress.getLocalHost().toString().split(".local/")[1];
    }


    public static String initHost() throws UnknownHostException {

        String  host = getHost();
        System.out.println("host is " + host);
        System.setProperty("java.rmi.server.hostname", host);
        return host;

    }



    public static Registry initRegistry(String host) throws UnknownHostException, RemoteException, AlreadyBoundException {
        Registry retVal = null;



            retVal = LocateRegistry.createRegistry(1077);


        if (retVal == null) {
            System.out.println("Could not initialize RMI registry...exiting...");
            System.exit(1);
        }

        return retVal;
    }





}
