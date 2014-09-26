package geometry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed Alabdullah on 9/24/14.
 */
public class CoordinateZone implements Serializable {

        List<CoordinateZone> subzones = new ArrayList<CoordinateZone>();

        private float findCenter(Point start, Point end) {

            float dist = end.distanceTo(start);
            return dist/2;


        }

        public float distanceFromCenterTo(Point point) {
            float centerX = findCenter(xStart, xEnd);

            float centerY = findCenter(yStart, yEnd);
            Point centroid = new Point(centerX, centerY);
            System.out.println("zone center is " + centroid);
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

        float x2 = randomPoint.getX();
        float y2 = randomPoint.getY();

        float x1 = xStart.getX();
        float y1 = xEnd.getY();

        boolean hasIt = (x1 <= x2 ) && ( y2 <= y1 );

        if (!hasIt) {
            for (CoordinateZone zone : subzones) {
                hasIt |= zone.hasPoint(randomPoint);
            }
            if (hasIt) {
                System.out.println("point in zone");
            }
            else {
                System.out.println("not in zone");
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
        float x = findCenter(xStart, yStart);
        Point newYStart = new Point(x, xStart.getY());
        Point newYEnd = new Point (x, xEnd.getY());
        CoordinateZone retVal = new CoordinateZone(newYStart, newYEnd, yStart, yEnd);
        yStart = newYStart;
        yEnd = newYEnd;
        return retVal;
    }

    public CoordinateZone splitHorizontally() {
        float y = findCenter(xStart, xEnd);
        Point newXEnd = new Point(xStart.getX(), y);
        Point newYEnd = new Point(xEnd.getX(), y);
        CoordinateZone retVal = new CoordinateZone(xStart, newXEnd, yStart, newYEnd);
        xEnd = newXEnd;
        yEnd = newYEnd;
        return retVal;
    }


    public CoordinateZone splitInHalf() {
        CoordinateZone retVal = null;
        if (isSquare()) {
            retVal = splitVertically();
        }
        else {
            retVal = splitHorizontally();
        }


        return retVal;

    }

    public boolean notAdjacentTo(CoordinateZone zone) {
       return zone.touchTop(this.bottom()) || zone.touchBottom(this.top()) || zone.touchLeft(this.right()) || zone.touchRight(this.left()) ;


    }


    private boolean touchVertically(Line first, Line second ) {

        Point fEnd = first.getEnd();
        Point sEnd = second.getEnd();

        if (fEnd.getX() != sEnd.getX()) {
            return false;
        }

        Point fStart = first.getStart();
        Point sStart = second.getStart();

        if (fStart.getY() <= sStart.getY() && fEnd.getY() >= sStart.getY()
            ||
                fStart.getY() >= sStart.getY() && fStart.getY() <= sEnd.getY())
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

        if (fStart.getX() <= sStart.getX() && fEnd.getX() >= sStart.getX()
                    ||
            fStart.getX() >= sStart.getX() && fStart.getX() <= sEnd.getX())

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
                return zone;
            }
        }
        return null;
    }
}

