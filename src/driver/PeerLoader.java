package driver;


import geometry.CoordinateZone;
import geometry.Point;
import peer.BootstrapPeer;
import peer.Peer;
import rmi.Network;
import rmi.RemoteLoggerStub;
import rmi.RemotePeerStub;

import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

import static debris.Constants.BOOTSTRAP;
import static debris.Constants.UNIVERSE;
import static peer.Peer.stub;


/**
 * Created by Ahmed Alabdullah on 9/23/14.
 */
public class PeerLoader  {

    public static Registry rmi;
    private static String host;
    private static String ip;

    private static void init() throws UnknownHostException, AlreadyBoundException, RemoteException {
        host = Network.initHost();
        ip = Network.getIPAddress();
        rmi = Network.initRegistry(host);
        connect();

    }

    private static void connect() throws UnknownHostException, AlreadyBoundException, RemoteException {


        RemotePeerStub _peer = null;
        if (BOOTSTRAP.equals(host)) {

            BootstrapPeer bootstrapPeer = new BootstrapPeer(host, ip);
            _peer = stub(bootstrapPeer);
            bootstrapPeer.setStub(_peer);
            bootstrapPeer.addOnlineNode(_peer);
            bootstrapPeer.setZone(new CoordinateZone(new Point(0,0), new Point(0,UNIVERSE), new Point(UNIVERSE,0), new Point(UNIVERSE,UNIVERSE)));
            RemoteLoggerStub logger = Network.initLogger();
            bootstrapPeer.setLogger(logger);

            //init distributed logger at bootstrap node


        }

        else {
            Peer peer = new Peer(host, ip);
            _peer = stub(peer);
            peer.setStub(_peer);
        }



        rmi.bind(host, _peer);
        System.out.println( host + " is online...\n");

    }


    public static void main(String[] args) {



        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }





    }






}



