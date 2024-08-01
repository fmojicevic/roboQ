package org.robo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class RobotStatus {
    @JsonProperty("visited")
    private List<Cell> visited = new ArrayList<>();

    @JsonProperty("cleaned")
    private List<Cell> cleaned = new ArrayList<>();

    @JsonProperty("final")
    private Start position;

    @JsonProperty("battery")
    private int battery;

    public List<Cell> getVisited() {
        return visited;
    }

    public void setVisited(List<Cell> visited) {
        this.visited = visited;
    }

    public List<Cell> getCleaned() {
        return cleaned;
    }

    public void setCleaned(List<Cell> cleaned) {
        this.cleaned = cleaned;
    }

    public Start getPosition() {
        return position;
    }

    public void setPosition(Start position) {
        this.position = position;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }
}
