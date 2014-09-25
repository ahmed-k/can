package peer;

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

        return (x1 < x2 ) && ( y2 < y1 );



    }
}

