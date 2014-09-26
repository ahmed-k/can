package test;

import geometry.CoordinateZone;
import geometry.Point;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Ahmed Alabdullah on 9/26/14.
 */
public class CoordinateZoneTests {


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
