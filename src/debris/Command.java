package debris;

/**
 * Created by Ahmed Alabdullah on 9/25/14.
 */
public enum Command {
    JOIN, LEAVE, INSERT, VIEW, SEARCH;



    public boolean is(String command) {

        if (this.name().equalsIgnoreCase(command)) {
            return true;
        }

        return false;

    }


}
