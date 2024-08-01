package org.robo;

public enum Direction {
    NORTH("N"),
    EAST("E"),
    SOUTH("S"),
    WEST("W");

    private final String heading;

    Direction(String heading) {
        this.heading = heading;
    }

    public String getHeading() {
        return heading;
    }

    public static Direction findByFacing(String facing) {
        for (Direction role : values()) {
            if (role.getHeading().equals(facing)) {
                return role;
            }
        }
        throw new IllegalArgumentException("No enum constant with facing " + facing);
    }
}
