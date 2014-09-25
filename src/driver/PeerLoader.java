package driver;


import peer.BootstrapPeer;
import peer.CoordinateZone;
import peer.Peer;
import peer.Point;
import rmi.Network;
import rmi.RemotePeerStub;

import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.Scanner;
import static debris.Constants.*;
import static peer.Peer.stub;


/**
 * Created by Ahmed Alabdullah on 9/23/14.
 */
public class PeerLoader  {



    private String name;
    public static Registry rmi;
    public static Scanner scanner = new Scanner(System.in);
    private static String host;
    //public static String prefix = "compute-0-";

    private static void init() throws UnknownHostException, AlreadyBoundException, RemoteException {
        host = java.net.InetAddress.getLocalHost().toString().split(".local")[0];
        System.out.println("host is " + host);
        System.setProperty("java.rmi.server.hostname", host);
        initRegistry();

    }

    private static void initRegistry() throws UnknownHostException, AlreadyBoundException, RemoteException {

        rmi = Network.initRegistry(host);
        RemotePeerStub _peer = null;
        if (BOOTSTRAP.equals(host)) {

            BootstrapPeer bootstrapPeer = new BootstrapPeer(host);
            _peer = stub(bootstrapPeer);
            bootstrapPeer.setStub(_peer);
            bootstrapPeer.addOnlineNode(_peer);
            bootstrapPeer.setZone(new CoordinateZone(new Point(0,0), new Point(0,UNIVERSE), new Point(UNIVERSE,0), new Point(UNIVERSE,UNIVERSE)));

        }

        else {
            Peer peer = new Peer(host);
            _peer = stub(peer);
            peer.setStub(_peer);
        }



        rmi.bind(host, _peer);
        System.out.println("Connected " + host + " to network...");

    }


    public static void main(String[] args) {



        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }





    }






}



