package debris;

/**
 * Created by Ahmed Alabdullah on 9/26/14.
 */
public class Logger {

    private static String content="";

    public static void log(String msg) {
        content += msg;
    }

    public static String deliverLog() {
        String retVal = content;
        content = "";
        return retVal;

    }


}
