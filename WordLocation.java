import java.awt.*;

public class WordLocation {
    private Point point;
    private Direction direction;
    private String word;
    private int overlapCount;

    WordLocation(Point point, Direction direction, String word) {
        overlapCount = 0;
        this.point = point;
        this.direction = direction;
        this.word = word;
    }

    WordLocation() {
        overlapCount = 0;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setOverlapCount(int overlapCount) {
        this.overlapCount = overlapCount;
    }

    public void incrementOverlapCount() {
        overlapCount++;
    }

    public Point getPoint() {
        return point;
    }

    public Direction getDirection() {
        return direction;
    }

    public String getWord() {
        return word;
    }

    public int getOverlapCount() {
        return overlapCount;
    }

    public String toString() {
        return (word + " starts at " + point.toString() + " and points " + direction);
    }
}
