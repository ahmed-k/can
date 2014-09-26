package geometry;

/**
 * Created by Ahmed Alabdullah on 9/25/14.
 */
public class Line {

    private Point start;
    private Point end;

    public Line(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    public Point getStart() {
        return start;
    }

    public void setStart(Point start) {
        this.start = start;
    }

    public Point getEnd() {
        return end;
    }

    public void setEnd(Point end) {
        this.end = end;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Line)) {
            return false;
        }
        Line other = (Line)o;
        Point oStart = other.getStart();
        Point oEnd = other.getEnd();
        return oStart.equals(start) && oEnd.equals(end);
    }

    public int hashCode() {
        return new Float( start.getX()+start.getY()+end.getX()+end.getY()).intValue();
    }
}
