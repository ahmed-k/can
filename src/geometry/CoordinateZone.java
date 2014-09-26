package geometry;

import java.io.Serializable;

/**
 * Created by Ahmed Alabdullah on 9/24/14.
 */
public class CoordinateZone implements Serializable {


        private float findCenter(Point start, Point end) {

            float dist = end.distanceTo(start);
            return dist/2;


        }

        public float distanceFromCenterTo(Point point) {
            float centerX = findCenter(xStart, xEnd);
            float centerY = findCenter(yStart, yEnd);

            return point.distanceTo(new Point(centerX, centerY));

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


    public boolean hasPoint(Point randomPoint) {

        float x2 = randomPoint.getX();
        float y2 = randomPoint.getY();

        float x1 = xStart.getX();
        float y1 = xEnd.getY();

        boolean hasIt = (x1 <= x2 ) && ( y2 <= y1 );
        if (hasIt) {
            System.out.println("point in zone");
        }
        else {
            System.out.println("not in zone");
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
}

