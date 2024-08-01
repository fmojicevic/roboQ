package org.robo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DataInput {
    @JsonProperty("map")
    private String[][] map;

    @JsonProperty("start")
    private Start start;

    @JsonProperty("commands")
    private List<String> commands;

    @JsonProperty("battery")
    private int battery;

    // Getters and Setters

    public String[][] getMap() {
        return map;
    }

    public void setMap(String[][] map) {
        this.map = map;
    }

    public Start getStart() {
        return start;
    }

    public void setStart(Start start) {
        this.start = start;
    }

    public List<String> getCommands() {
        return commands;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }
}
