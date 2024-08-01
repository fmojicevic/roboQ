package org.robo;

public enum Command {
    TURN_LEFT("TL", 1),
    TURN_RIGHT("TR", 1),
    ADVANCE("A", 2),
    BACK("B", 3),
    CLEAN("C", 5);

    private final String action;
    private final int consumption;

    Command(String action, int consumption) {
        this.action = action;
        this.consumption = consumption;
    }

    public String getAction() {
        return action;
    }

    public int getConsumption() {
        return consumption;
    }

    public static Command findByAction(String action) {
        for (Command role : values()) {
            if (role.getAction().equals(action)) {
                return role;
            }
        }
        throw new IllegalArgumentException("No enum constant with action " + action);
    }
}
