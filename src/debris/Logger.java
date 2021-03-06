package debris;

import rmi.RemoteLoggerStub;

import java.rmi.RemoteException;

/**
 * Created by Ahmed Alabdullah on 9/26/14.
 */
public class Logger implements RemoteLoggerStub {

    private static String content="";
    private static Logger instance;



    private Logger() {
        content="";
    }




    public static Logger createInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    @Override
    public String deliverLog() throws RemoteException {
        String retVal = content;
        content = "";
        return retVal;

    }


    @Override
    public void log(String msg) throws RemoteException {
        content += msg+"\n";
    }
}
