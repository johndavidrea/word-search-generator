public enum Direction {
    EAST(1, 0, "East"),
    SOUTHEAST(1, 1, "Southeast"),
    SOUTH(0, 1, "South"),
    SOUTHWEST(-1, 1, "Southwest"),
    WEST(-1, 0, "West"),
    NORTHWEST(-1, -1, "Northwest"),
    NORTH(0, -1, "North"),
    NORTHEAST(1, -1, "Northeast");

    int xOffset;
    int yOffset;
    String name;

    Direction(int xOffset, int yOffset, String name) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
