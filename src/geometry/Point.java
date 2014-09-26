package geometry;

import java.io.Serializable;

/**
 * Created by Ahmed Alabdullah on 9/25/14.
 */
public class Point implements Serializable{


    public float distanceTo(Point point ) {

        float x2 = point.getX();
        float y2 = point.getY();

        return (float) Math.sqrt((x2-x)*(x2-x) + (y2-y)*(y2-y));


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

    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
