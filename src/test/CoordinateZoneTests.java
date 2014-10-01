package test;

import geometry.CoordinateZone;
import geometry.Point;
import org.junit.Test;
import peer.Peer;

import java.rmi.RemoteException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Ahmed Alabdullah on 9/26/14.
 */
public class CoordinateZoneTests {


    @Test public void itShouldRecognizeParallelIsNotAdjacent() {
        CoordinateZone zone1 = new CoordinateZone(new Point(0,0), new Point(0,10), new Point(5,0), new Point(5,10));
        CoordinateZone zone2 = new CoordinateZone(new Point(5,0), new Point(5,5), new Point(7.5f,0), new Point(7.5f,5));
        CoordinateZone zone3 = new CoordinateZone(new Point(5,5), new Point(5,10), new Point(10,5), new Point(10,10));
        CoordinateZone zone4 = new CoordinateZone(new Point(7.5f,0), new Point(7.5f,5), new Point(10,0), new Point(10,5));

        assertFalse(zone4.adjacentTo(zone1));
        assertTrue(zone4.adjacentTo(zone2));
        assertTrue(zone4.adjacentTo(zone3));
        assertFalse(zone1.adjacentTo(zone4));
        assertTrue(zone1.adjacentTo(zone3));
        assertTrue(zone1.adjacentTo(zone2));
        assertTrue(zone2.adjacentTo(zone1));
        assertTrue(zone2.adjacentTo(zone3));
        assertTrue(zone2.adjacentTo(zone4));
        assertTrue(zone3.adjacentTo(zone1));
        assertTrue(zone3.adjacentTo(zone2));
        assertTrue(zone3.adjacentTo(zone4));
    }


    @Test public void itShouldRecognizeDiagonalAdjacencyIsNotNeighborhood() throws RemoteException {
        CoordinateZone zone1 = new CoordinateZone(new Point(0,0), new Point(0,5), new Point(5,0), new Point(5,5));
        CoordinateZone zone2 = new CoordinateZone(new Point(5,0), new Point(5,5), new Point(10,0), new Point(10,5));
        CoordinateZone zone3 = new CoordinateZone(new Point(0,5), new Point(0,10), new Point(5,5), new Point(5,10));
        CoordinateZone zone4 = new CoordinateZone(new Point(5,5), new Point(5,10), new Point(10,5), new Point(10,10));

        Peer p1 = new Peer("peer4", zone3);

        assertFalse(!p1.doesntTouch(zone2));

        assertTrue(zone1.adjacentTo(zone2));
        assertTrue(zone1.adjacentTo(zone3));
        assertFalse(zone1.adjacentTo(zone4));
        assertTrue(zone2.adjacentTo(zone1));
        assertTrue(zone2.adjacentTo(zone4));
        assertFalse(zone2.adjacentTo(zone3));
        assertTrue(zone3.adjacentTo(zone1));
        assertTrue(zone3.adjacentTo(zone4));
        assertFalse(zone3.adjacentTo(zone2));
        assertTrue(zone4.adjacentTo(zone2));
        assertTrue(zone4.adjacentTo(zone3));
        assertFalse(zone4.adjacentTo(zone1));


    }

    @Test public void itShouldFindCenterOfZoneAccurately() {
        CoordinateZone zone = new CoordinateZone(new Point(0,0), new Point(0,10), new Point (10,0), new Point(10,10));
        assertTrue("fails with distance = " + zone.distanceFromCenterTo(new Point(5, 5)), 0f == zone.distanceFromCenterTo(new Point(5, 5)));
    }


    @Test public void itShouldBeAbleToUnderstandWhichZonesTouchIt() {

        CoordinateZone zone = new CoordinateZone(new Point(0,0), new Point(0,10), new Point(10,0), new Point(10,10));
        CoordinateZone zone2 = zone.splitInHalf();
        CoordinateZone zone3 = zone2.splitInHalf();
        assertTrue(zone.adjacentTo(zone2));
        assertTrue(zone.adjacentTo(zone3));



    }

    @Test public void itShouldDetermineIfItHasAPoint() {
        Point point = new Point(5, 6);
        CoordinateZone zone = new CoordinateZone(new Point(0, 5), new Point(0,10), new Point(10,5), new Point(10, 10));
        assertTrue(zone.hasPoint(point));
    }

    @Test
    public void itShouldSplitAccurately() {
        CoordinateZone zone = new CoordinateZone(new Point(0,0), new Point(0,10), new Point(10,0), new Point(10,10));
        CoordinateZone zone2 = zone.splitInHalf();
        CoordinateZone zone3 = zone2.splitInHalf();
        System.out.println("zone 1: "+ zone);
        System.out.println("zone 2: "+ zone2);
        System.out.println("zone 3: "+ zone3);
        zone2.merge(zone3);
        System.out.println(zone2);
    }

    @Test public void itShouldGetPointAccurately() {
        CoordinateZone zone1 = new CoordinateZone(new Point(0,0), new Point(0,10), new Point(10,0), new Point(10,10));
        CoordinateZone zone2 = zone1.splitInHalf();
        CoordinateZone zone3 = zone2.splitInHalf();
        Point p1 = new Point(2,2);
        assertTrue(zone1.hasPoint(p1));
        assertFalse(zone2.hasPoint(p1));
        assertFalse(zone3.hasPoint(p1));
        Point p2 = new Point (10,10);
        assertFalse(zone1.hasPoint(p2));
        assertFalse(zone2.hasPoint(p2));
        assertTrue(zone3.hasPoint(p2));

    }


}
