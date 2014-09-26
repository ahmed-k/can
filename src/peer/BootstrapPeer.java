package peer;

import rmi.RemotePeerStub;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed Alabdullah on 9/25/14.
 */
public class BootstrapPeer extends Peer {

    private List<RemotePeerStub> onlineNodes = new ArrayList<RemotePeerStub>();

    public BootstrapPeer(String name, String ip) {
        super(name,ip);
    }


    public void addOnlineNode(RemotePeerStub peer) {
        onlineNodes.add(peer);
    }

    public List<RemotePeerStub> findAvailableNodes() {
        return onlineNodes;
    }

    public void removeOnlineNode(RemotePeerStub peer) {
        onlineNodes.remove(peer);
    }
}
