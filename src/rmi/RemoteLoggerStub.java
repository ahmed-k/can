package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Ahmed Alabdullah on 9/30/14.
 */
public interface RemoteLoggerStub extends Remote {

    void log(String msg) throws RemoteException;

    String deliverLog();
}
