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

import static debris.Command.*;
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

        Command command = null;
            while (true) {
                System.out.println("Please enter a command");
                System.out.print(">>");
                try {
                    String[] input = scanner.nextLine().split(" ");
                    if (input.length > 0) {
                        command = Command.valueOf(input[0].toUpperCase());

                        if (command == EXIT) {
                            break;
                        }

                        if (command == INSERT) {
                            if (input.length < 3) {
                                System.out.println("Usage: INSERT keyword peer");
                            }
                            else {
                                String keyword = input[1];
                                String peerId = input[2];
                                insert(keyword, peerId);
                            }

                        }

                        if (command == SEARCH) {
                            if (input.length < 3) {
                                System.out.println("Usage: SEARCH keyword peer");
                            }
                            else {
                                String keyword = input[1];
                                String peerId = input[2];
                                search(keyword, peerId);
                            }
                        }

                        if (command == LEAVE) {
                            if (input.length < 2) {
                                System.out.println("Usage: LEAVE peer");
                            }
                            String peerId = input[1];
                            leave(peerId);
                        }

                        if (command == VIEW) {

                            if (input.length == 1) {
                                viewAll();
                            }
                            else if (input.length == 2) {
                                view(input[1]);
                            }
                            else {
                                System.out.println("Usage: VIEW [peer]");
                            }
                        }


                        if (command == JOIN) {
                            if (input.length < 1 || input.length > 2) {
                                System.out.println("Usage: JOIN [peer] ");
                            }
                            else if (input.length == 2) {
                                String peerId = scanner.next();
                                join(peerId);
                            }

                            else {
                                for (int i=2; i<21; i++) {
                                  join("compute-0-"+i);
                                }
                            }




                        }
                    }
                }
                catch(IllegalArgumentException ex) {
                    System.out.println("Not a valid command...");
                }


            }



    }


    private static void viewAll() throws RemoteException, NotBoundException, UnknownHostException {
        RemotePeerStub bootstrap = getPeer(BOOTSTRAP);
        List<RemotePeerStub> nodes = bootstrap.findAvailableNodes();

        for (RemotePeerStub node: nodes) {
            System.out.println(node.info());
        }

    }

    private static void view(String peerId) throws RemoteException, NotBoundException, UnknownHostException {
        RemotePeerStub peer = getPeer(peerId);
        System.out.println(peer.info());

    }

    private static void leave(String peerId) throws RemoteException, NotBoundException, UnknownHostException {
        System.out.println("Executing LEAVE request...");
        RemotePeerStub peer = getPeer(peerId);
        String log = peer.leave();
        RemotePeerStub bootstrap = getPeer(BOOTSTRAP);
        bootstrap.removeOnlineNode(peer);

    }

    private static void join(String peerId) throws RemoteException, NotBoundException, UnknownHostException {
        System.out.println("Executing JOIN request...");
        RemotePeerStub bootstrap = getPeer(BOOTSTRAP);

            RemotePeerStub peer = null;
        try {
            peer = getPeer(peerId);
        }
        //node not active
        catch(RemoteException e) {
            return;
        }

            List<RemotePeerStub> onlineNodes = bootstrap.findAvailableNodes();
            boolean alreadyJoined = false;
            for (RemotePeerStub node: onlineNodes) {

                if (node.ip().equals(peer.ip())) {
                    System.out.println("Peer already part of the network...");
                    alreadyJoined = true;
                    break;
                }

            }
            if (!alreadyJoined) {
                Point randomPoint = peer.pickRandomPoint();
                System.out.println("Picked random point: " + randomPoint);
                RemotePeerStub router = pickRandomOnlineNode(onlineNodes);
                System.out.println("Picked random online node: " + router.desc());
                RemotePeerStub pointOwnerPeer = router.route(randomPoint);
                pointOwnerPeer.accomodate(peer, randomPoint);
                bootstrap.addOnlineNode(peer);
            }




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
        System.out.println(keyword + " resolved as " + insertionPoint);
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
        return new Point(odd % 10 ,even % 10);
    }

    private static RemotePeerStub pickRandomOnlineNode(List<RemotePeerStub> nodes) {

        Random r = new Random();
        int index = r.nextInt(nodes.size());
        return nodes.get(index);

    }


}
