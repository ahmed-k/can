package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Ahmed Alabdullah on 9/23/14.
 */
public interface RemotePeerStub extends Remote {


    public String sayHi() throws RemoteException;
    public void send(String msg);

}
