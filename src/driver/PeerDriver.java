package driver;

import debris.Command;
import peer.Point;
import rmi.Network;
import rmi.RemotePeerStub;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static debris.Command.JOIN;
import static debris.Constants.BOOTSTRAP;

/**
 * Created by Ahmed Alabdullah on 9/24/14.
 */
public class PeerDriver {

    public static Scanner scanner = new Scanner(System.in);
    public static Registry rmi;
    public static void main(String[] args) throws RemoteException, NotBoundException {


        try {
            rmi = Network.getRegistry();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        Command command = null;
            while (true) {
                try {
                    command = Command.valueOf(scanner.next());
                }
                catch(IllegalArgumentException ex) {
                    System.out.println("Not a valid command...");
                }
               if (command == JOIN ) {
                  String peerId = scanner.next();
                   while (peerId == null) {
                       System.out.println("Please specify a peer...");
                       peerId = scanner.next();

                   }

                   join(peerId);



               }
            }



    }

    private static void join(String peerId) throws RemoteException, NotBoundException {

        RemotePeerStub bootstrap = getPeer(BOOTSTRAP);
        RemotePeerStub peer = getPeer(peerId);
        List<RemotePeerStub> onlineNodes = bootstrap.findAvailableNodes();
        Point randomPoint = peer.pickRandomPoint();
        RemotePeerStub router = pickRandomOnlineNode(onlineNodes);
        RemotePeerStub pointOwner = router.route(randomPoint);
        pointOwner.splitZone(peer);

    }


    private static RemotePeerStub getPeer(String peerName) throws RemoteException, NotBoundException {
        return (RemotePeerStub) rmi.lookup(peerName);
    }

    private static RemotePeerStub pickRandomOnlineNode(List<RemotePeerStub> nodes) {

        Random r = new Random();
        int index = r.nextInt(nodes.size());
        return nodes.get(index);

    }


}