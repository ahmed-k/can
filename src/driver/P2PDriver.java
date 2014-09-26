package driver;

import debris.Command;
import geometry.Point;
import rmi.Network;
import rmi.RemotePeerStub;

import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static debris.Command.INSERT;
import static debris.Command.SEARCH;
import static debris.Command.JOIN;
import static debris.Constants.BOOTSTRAP;

/**
 * Created by Ahmed Alabdullah on 9/24/14.
 */
public class P2PDriver {

    public static Scanner scanner = new Scanner(System.in);
    public static Registry rmi;
    public static void main(String[] args) throws RemoteException, NotBoundException, UnknownHostException {


        try {
            rmi = Network.getRegistry();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("You have connected to the P2P network..");
        System.out.println("Please enter a command");
        System.out.print(">>");
        Command command = null;
            while (true) {
                try {
                    command = Command.valueOf(scanner.next().toUpperCase());
                    if (command == INSERT) {

                        String keyword = scanner.next();
                        String peerId = scanner.next();
                        while (keyword == null) {
                            System.out.println("Please specify a keyword...");
                            keyword = scanner.next();
                        }

                        insert(keyword, peerId);


                    }

                    if (command == SEARCH) {
                        String keyword = scanner.next();
                        String peerId = scanner.next();

                        search(keyword, peerId);

                    }




                if (command == JOIN) {
                    String peerId = scanner.next();
                    while (peerId == null) {
                        System.out.println("Please specify a peer...");
                        peerId = scanner.next();

                    }

                    join(peerId);


                }
            }
                catch(IllegalArgumentException ex) {
                    System.out.println("Not a valid command...");
                }


            }



    }

    private static void join(String peerId) throws RemoteException, NotBoundException, UnknownHostException {
        System.out.println("Executing JOIN request...");
        RemotePeerStub bootstrap = getPeer(BOOTSTRAP);
        RemotePeerStub peer = getPeer(peerId);
        List<RemotePeerStub> onlineNodes = bootstrap.findAvailableNodes();
        Point randomPoint = peer.pickRandomPoint();
        System.out.println("Picked random point: " + randomPoint);
        RemotePeerStub router = pickRandomOnlineNode(onlineNodes);
        System.out.println("Picked random online node: " + router.desc());
        RemotePeerStub pointOwnerPeer = router.route(randomPoint);
        pointOwnerPeer.accomodate(peer);

    }


    private static void search(String keyword, String peerId) throws RemoteException, NotBoundException, UnknownHostException {
        System.out.println("Executing SEARCH request...");
        Point insertionPoint = resolve(keyword);
        RemotePeerStub peer = getPeer(peerId);
        peer.search(insertionPoint, keyword);

    }

    private static void insert(String keyword, String peerId) throws RemoteException, NotBoundException, UnknownHostException {
        System.out.println("Executing INSERT request...");
        Point insertionPoint = resolve(keyword);
        RemotePeerStub peer = getPeer(peerId);
        peer.insert(insertionPoint, keyword);

    }


    private static RemotePeerStub getPeer(String peerName) throws RemoteException, NotBoundException, UnknownHostException {
        rmi = Network.getRegistry(peerName);
        return (RemotePeerStub) rmi.lookup(peerName);
    }

    private static Point resolve(String keyword) {

        char[] arr = keyword.toCharArray();
        float even=0;
        float odd=0;
        for (int i=0 ; i< arr.length; i++) {
            if (i % 2 == 0 ) {
                even += arr[i];
            }
            else {
                odd += arr[i];
            }
        }
        return new Point(odd,even);
    }

    private static RemotePeerStub pickRandomOnlineNode(List<RemotePeerStub> nodes) {

        Random r = new Random();
        int index = r.nextInt(nodes.size());
        return nodes.get(index);

    }


}
