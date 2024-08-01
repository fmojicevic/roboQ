package org.robo;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class Robot {
    private static final Logger logger = Logger.getLogger(Robot.class.getName());

    private final RobotStatus status = new RobotStatus();
    private final String outputFilePath;
    private final String[][] map;
    private final Cell position;
    private final List<Command> mainCommands;
    boolean stuck = false;
    private Direction direction;
    private int battery;

    public Robot(String outputFilePath, String[][] map, Start start, List<String> mainCommands, int battery) {
        this.outputFilePath = outputFilePath;
        this.map = map;
        position = new Cell(start.getX(), start.getY());
        direction = Direction.findByFacing(start.getFacing());
        this.mainCommands = mainCommands.stream().map(Command::findByAction).toList();
        this.battery = battery;

        status.getVisited().add(new Cell(position));
    }

    public void start() {
        executeCommands(mainCommands, false);
        exit();
    }

    /**
     * Method executing the list of the commands with backOff flag to cover special case checks - in case of backOff it does not proceed when the obstacle is
     * hit.
     *
     * @param commands to execute
     * @param backOff  flag differentiating regular commands from back off sequence
     */
    private void executeCommands(List<Command> commands, boolean backOff) {
        for (Command command : commands) {
            logger.info("Next command to try: " + command.getAction() + (backOff ? " (performing back off)" : ""));
            if (battery < command.getConsumption()) {
                exit();
            }
            battery -= command.getConsumption();

            switch (command) {
                case TURN_LEFT -> turn(true);
                case TURN_RIGHT -> turn(false);
                case ADVANCE -> {
                    stuck = !move(false);
                    if (stuck) {
                        if (backOff) {
                            return;
                        }
                        performBackOff();
                    }
                }
                case BACK -> {
                    stuck = !move(true);
                    if (stuck) {
                        if (backOff) {
                            return;
                        }
                        performBackOff();
                    }
                }
                case CLEAN -> {
                    if (!status.getCleaned().contains(position)) {
                        status.getCleaned().add(new Cell(position));
                    }
                }
            }

        }
    }

    /**
     * When regular command hits an obstacle, back off procedure is called. Each sublist of commands is executed until robot is 'free'. In case it remains
     * stuck at the end exit is called.
     */
    private void performBackOff() {
        List<List<Command>> sequences = Arrays.asList(
                Stream.of("TR", "A", "TL").map(Command::findByAction).toList(),
                Stream.of("TR", "A", "TR").map(Command::findByAction).toList(),
                Stream.of("TR", "A", "TR").map(Command::findByAction).toList(),
                Stream.of("TR", "B", "TR", "A").map(Command::findByAction).toList(),
                Stream.of("TL", "TL", "A").map(Command::findByAction).toList()
        );

        for (List<Command> seq : sequences) {
            executeCommands(seq, true);
            if (!stuck) {
                break;
            }
        }
        if (stuck) {
            exit();
        }
    }

    /**
     * Updates the status with position and battery status before writing it to the result file.
     */
    private void exit() {
        status.setPosition(new Start(position.getX(), position.getY(), direction.getHeading()));
        status.setBattery(battery);

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(new java.io.File(outputFilePath), status);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Changing the orientation by 90 degrees
     * @param left condition to torn to the left (if true) or to the right (false).
     */
    private void turn(boolean left) {
        switch (direction) {
            case NORTH -> direction = left ? Direction.WEST : Direction.EAST;
            case WEST -> direction = left ? Direction.SOUTH : Direction.NORTH;
            case SOUTH -> direction = left ? Direction.EAST : Direction.WEST;
            case EAST -> direction = left ? Direction.NORTH : Direction.SOUTH;
        }
    }

    /**
     * Checks if robot can make a move and changes its position if possible.
      * @param backwards if true, tries to make the move in the opposite direction.
     * @return true if robot changed the position, false otherwise.
     */
    private boolean move(boolean backwards) {
        boolean moved = true;
        Direction tmpDirection = direction;
        if (backwards) {
            tmpDirection = flipDirection(tmpDirection);
        }
        switch (tmpDirection) {
            case NORTH -> {
                if (position.getY() == 0
                        || map[position.getY() - 1][position.getX()].equals("C")
                        || map[position.getY() - 1][position.getX()].equals("null")
                        || map[position.getY() - 1][position.getX()].equals(null)) {
                    moved = false;
                } else {
                    position.setY(position.getY() - 1);
                }
            }
            case EAST -> {
                if (position.getX() == map[position.getY()].length - 1
                        || map[position.getY()][position.getX() + 1].equals("C")
                        || map[position.getY()][position.getX() + 1].equals("null")
                        || map[position.getY()][position.getX() + 1] == null) {
                    moved = false;
                } else {
                    position.setX(position.getX() + 1);
                }
            }
            case SOUTH -> {
                if (position.getY() == map.length - 1
                        || map[position.getY() + 1][position.getX()].equals("C")
                        || map[position.getY() + 1][position.getX()].equals("null")
                        || map[position.getY() + 1][position.getX()].equals(null)) {
                    moved = false;
                } else {
                    position.setY(position.getY() + 1);
                }
            }
            case WEST -> {
                if (position.getX() == 0
                        || map[position.getY()][position.getX() - 1].equals("C")
                        || map[position.getY()][position.getX() - 1].equals("null")
                        || map[position.getY()][position.getX() - 1] == null) {
                    moved = false;
                } else {
                    position.setX(position.getX() - 1);
                }
            }
        }
        if (moved && (!status.getVisited().contains(position))) {
            status.getVisited().add(new Cell(position));
        }
        return moved;
    }

    /**
     * Provides the opposite direction of the given one.
     * @param direction original direction
     * @return opposite of the original
     */
    private Direction flipDirection(Direction direction) {
        Direction res = direction;
        switch (direction) {
            case NORTH -> res = Direction.SOUTH;
            case EAST -> res = Direction.WEST;
            case SOUTH -> res = Direction.NORTH;
            case WEST -> res = Direction.EAST;
        }
        return res;
    }
}
