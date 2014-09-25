package rmi;

import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static debris.Constants.BOOTSTRAP;

/**
 * Created by Ahmed Alabdullah on 9/25/14.
 */
public class Network {

    private static Registry rmi;

    public static Registry getRegistry() throws RemoteException {

        return LocateRegistry.getRegistry(BOOTSTRAP, 1077);

    }




    public static Registry initRegistry(String host) throws UnknownHostException, RemoteException, AlreadyBoundException {
        Registry retVal = null;


        if (BOOTSTRAP.equals(host)) {
            retVal = LocateRegistry.createRegistry(1077);

        }
        else {
            retVal = LocateRegistry.getRegistry(BOOTSTRAP,1077);

        }

        if (retVal == null) {
            System.out.println("Could not initialize RMI registry...exiting...");
            System.exit(1);
        }

        return retVal;
    }


    public static RemotePeerStub find(String node) throws RemoteException, NotBoundException {
        return (RemotePeerStub) rmi.lookup(node);
    }


}
