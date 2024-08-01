package org.robo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Start extends Cell {
    @JsonProperty("facing")
    private String facing;

    public Start() {
    }

    public Start(int x, int y, String facing) {
        super(x, y);
        this.facing = facing;
    }

    public String getFacing() {
        return facing;
    }

    public void setFacing(String facing) {
        this.facing = facing;
    }
}
