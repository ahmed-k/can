package geometry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed Alabdullah on 9/24/14.
 */
public class CoordinateZone implements Serializable {

        List<CoordinateZone> subzones = new ArrayList<CoordinateZone>();



        public float distanceFromCenterTo(Point point) {
            Point centroidX  = new Line(xStart, yStart).findCenter();
            Point centroidY = new Line(yStart, yEnd).findCenter();
            Point centroid = new Point(centroidX.getX(), centroidY.getY());
            //System.out.println("zone center is " + centroid);
            return point.distanceTo(centroid);

        }

        public CoordinateZone(Point xStart, Point xEnd, Point yStart, Point yEnd) {

            this.xStart = xStart;
            this.xEnd   = xEnd;
            this.yStart = yStart;
            this.yEnd = yEnd;
        }
        private Point xStart;
        private Point xEnd;
        private Point yStart;
        private Point yEnd;

    public Point getXStart() {
        return xStart;
    }

    public Point getYStart() {
        return yStart;
    }

    public Point getXEnd() {
        return xEnd;
    }

    public Point getYEnd(){
        return yEnd;
    }




    public boolean hasPoint(Point randomPoint) {

        float x = randomPoint.getX(); //5
        float y = randomPoint.getY(); //6

        float xRangeBegin = xStart.getX(); //0
        float xRangeEnd = yStart.getX(); //10
        float yRangeBegin = xStart.getY(); //5
        float yRangeEnd = xEnd.getY(); //10
        boolean withinX = ( xRangeBegin <= x && x <= xRangeEnd );
        boolean withinY = ( yRangeBegin <= y && y <= yRangeEnd );


        boolean hasIt = (withinX && withinY );

        if (!hasIt) {
            for (CoordinateZone zone : subzones) {
                hasIt |= zone.hasPoint(randomPoint);
            }
        }
        return hasIt;

    }

    public String toString() {
       return "ZONE: xStart " + xStart.toString() + ", xEnd : " + xEnd.toString() + ", yStart :" + yStart.toString() + " , yEnd : " + yEnd.toString();

    }

    public boolean isSquare() {
        return (xStart.distanceTo(xEnd) == xStart.distanceTo(yStart));
    }

    public CoordinateZone splitVertically() {
        Point centroid = new Line(xStart, yStart).findCenter();
        Point newYStart = centroid;
        Point newYEnd = new Point (centroid.getX(), xEnd.getY());
        CoordinateZone retVal = new CoordinateZone(newYStart, newYEnd, yStart, yEnd);
        yStart = newYStart;
        yEnd = newYEnd;
        return retVal;
    }

    public CoordinateZone splitHorizontally() {
        Point centroid = new Line(xStart, xEnd).findCenter();
        Point newXEnd = new Point(xStart.getX(), centroid.getY());
        Point newYEnd = new Point(yStart.getX(), centroid.getY());
        CoordinateZone retVal = new CoordinateZone(newXEnd, xEnd, newYEnd, yEnd);
        xEnd = newXEnd;
        yEnd = newYEnd;
        return retVal;
    }


    public CoordinateZone splitInHalf() {
        CoordinateZone retVal = null;
        if (isSquare() || (xStart.distanceTo(yStart) > xStart.distanceTo(xEnd)) ) {
            retVal = splitVertically();
        }
        else {
            retVal = splitHorizontally();
        }


        return retVal;

    }

    public boolean adjacentTo(CoordinateZone zone) {
       return zone.touchTop(this.bottom()) || zone.touchBottom(this.top()) || zone.touchLeft(this.right()) || zone.touchRight(this.left()) ;


    }

    public boolean notAdjacentTo(CoordinateZone zone) {
        return !adjacentTo(zone);
    }


    private boolean touchVertically(Line first, Line second ) {

        Point fEnd = first.getEnd();
        Point sEnd = second.getEnd();

        if (fEnd.getX() != sEnd.getX()) {
            return false;
        }

        Point fStart = first.getStart();
        Point sStart = second.getStart();

        if ((fStart.getY() <= sStart.getY() && fEnd.getY() >= sStart.getY() && fEnd.distanceTo(sStart) != 0)
            ||
        ( fStart.getY() >= sStart.getY() && fStart.getY() <= sEnd.getY() && fStart.distanceTo(sEnd) !=0 ) )
        {
            return true;
        }

        return false;
    }

    private boolean touchLeft(Line right) {
        return touchVertically(right, left());
    }

    private boolean touchRight(Line left) {
        return touchVertically(left, right());
    }

    private boolean touchHorizontally(Line first, Line second) {
        Point fEnd = first.getEnd();
        Point sEnd = second.getEnd();

        if (fEnd.getY() != sEnd.getY()) {
            return false;
        }

        Point fStart = first.getStart();
        Point sStart = second.getStart();

        if ((fStart.getX() <= sStart.getX() && fEnd.getX() >= sStart.getX() && fEnd.distanceTo(sStart) != 0)
                    ||
            fStart.getX() >= sStart.getX() && fEnd.getX() <= sEnd.getX() && sEnd.distanceTo(fStart) != 0)

        {
            return  true;
        }

        return false;


    }




    public boolean touchBottom(Line top) {
        return touchHorizontally(top, bottom());
    }

    public boolean touchTop(Line bottom) {
       return touchHorizontally(top(), bottom);
    }


    public Line left() {
        return new Line(xStart, xEnd);
    }

    public Line right() {
        return new Line(yStart,yEnd);
    }

    public Line top() {
        return new Line(xEnd, yEnd);
    }

    public Line bottom() {
        return new Line(xStart, yStart);
    }

    public Float size() {
        float length = xStart.distanceTo(xEnd);
        float width = xStart.distanceTo(yStart);
        return length*width;
    }

    public boolean willMergeUniformly(CoordinateZone zone) {

        if (   shareBottom(zone) || shareTop(zone) || shareRight(zone) || shareLeft(zone)) {
            return true;
        }
                return false;
    }

    private boolean shareLeft(CoordinateZone zone) {
        return  zone.right().equals(left());
    }

    private boolean shareRight(CoordinateZone zone) {
       return  zone.left().equals(right());
    }

    private boolean shareTop(CoordinateZone zone) {
        return zone.bottom().equals(top());
    }

    private boolean shareBottom(CoordinateZone zone) {
        return  zone.top().equals(bottom());
    }

    private void uniformMerge(CoordinateZone other) {

        if (shareBottom(other)) {
            xStart = other.getXStart();
            yStart = other.getYStart();
        }
        else if (shareTop(other)) {
            xEnd = other.getXEnd();
            yEnd = other.getYEnd();
        }
        else if (shareLeft(other)) {
            xStart = other.getXStart();
            xEnd = other.getXEnd();
        }

        else if (shareRight(other)) {
            yStart = other.getYStart();
            yEnd = other.getYEnd();
        }




    }


    public void merge(CoordinateZone zone) {
        if (willMergeUniformly(zone)) {
            uniformMerge(zone);
        }
        else {
            //add zone here temporarily
            subzones.add(zone);
        }
    }

    public boolean inASubZone(Point peerPoint) {

        for (CoordinateZone zone : subzones) {
            if (zone.hasPoint(peerPoint)) {
                return true;
            }
        }
        return false;
    }

    public CoordinateZone getSubzoneOwning(Point peerPoint) {
        for (CoordinateZone zone : subzones) {
            if (zone.hasPoint(peerPoint)) {
                subzones.remove(zone);
                return zone;

            }
        }
        return null;
    }

    public Float proximityTo(Point randomPoint) {
        Float proximity = this.distanceFromCenterTo(randomPoint);

        for (CoordinateZone subzone : subzones) {
            Float subzoneDistance = subzone.distanceFromCenterTo(randomPoint);
            proximity = (subzoneDistance < proximity) ? subzoneDistance : proximity;
        }
        return proximity;
    }
}

