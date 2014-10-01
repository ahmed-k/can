package debris;

import rmi.RemoteLoggerStub;

import java.rmi.RemoteException;

/**
 * Created by Ahmed Alabdullah on 9/26/14.
 */
public class Logger implements RemoteLoggerStub {

    private static String content="";
    private Logger instance;
    private static int i;



    private Logger() {
        content="";
        i=0;
    }




    public Logger createLogger() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }


    public String deliverLog() {
        String retVal = content;
        content = "";
        return retVal;

    }


    @Override
    public void log(String msg) throws RemoteException {
        content += i+"." + msg+"\n";
        i++;
    }
}
