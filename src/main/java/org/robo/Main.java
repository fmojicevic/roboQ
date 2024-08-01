package org.robo;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args == null || args.length != 2) {
            return;
        }

        ObjectMapper mapper = new ObjectMapper();

        try {
            DataInput dataInput = mapper.readValue(new File(args[0]), DataInput.class);
            Robot robot = new Robot(args[1], dataInput.getMap(), dataInput.getStart(), dataInput.getCommands(), dataInput.getBattery());

            robot.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}