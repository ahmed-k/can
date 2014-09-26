package geometry;

import java.io.Serializable;

/**
 * Created by Ahmed Alabdullah on 9/25/14.
 */
public class Point implements Serializable{


    public float distanceTo(Point point ) {

        float x2 = point.getX();
        float y2 = point.getY();
        float distance = (float) Math.sqrt((x2-x)*(x2-x) + (y2-y)*(y2-y));
        System.out.println("Distance is "  + distance);
        return distance;


    }

    private float x;
    private float y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Point)) {
            return false;
        }

        Point other = (Point) o;
        return other.getX() == x && other.getY() == y;
    }

    public int hashCode() {
        return new Float(x+y*17).intValue();
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
