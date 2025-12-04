package org.java.design.pattern.behavioral.command;

import java.util.ArrayList;
import java.util.List;

// Receiver - the object that performs actual work
class Light {
    public void on() {
        System.out.println("Light is ON");
    }

    public void off() {
        System.out.println("Light is OFF");
    }
}

// Command interface - encapsulates a request
interface Command {
    void execute();

    void undo();
}

// Concrete commands
class LightOnCommand implements Command {
    private Light light;

    public LightOnCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.on();
    }

    @Override
    public void undo() {
        light.off();
    }
}

class LightOffCommand implements Command {
    private Light light;

    public LightOffCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.off();
    }

    @Override
    public void undo() {
        light.on();
    }
}

// Invoker - executes commands
class RemoteControl {
    private List<Command> commandHistory = new ArrayList<>();

    public void pressButton(Command command) {
        command.execute();
        commandHistory.add(command);
    }

    public void undo() {
        if (commandHistory.isEmpty()) {
            System.out.println("No commands to undo!");
            return;
        }

        Command lastCommand = commandHistory.remove(commandHistory.size() - 1);
        lastCommand.undo();
    }

    public void printHistory() {
        System.out.println("Command history size: " + commandHistory.size());
    }
}

// Client code
public class CommandPatternDemo {
    public static void main(String[] args) {
        // Create receiver
        Light livingRoomLight = new Light();

        // Create commands
        Command lightOn = new LightOnCommand(livingRoomLight);
        Command lightOff = new LightOffCommand(livingRoomLight);

        // Create invoker
        RemoteControl remote = new RemoteControl();

        // Execute commands
        System.out.println("=== Executing Commands ===");
        remote.pressButton(lightOn);
        remote.pressButton(lightOff);
        remote.pressButton(lightOn);

        System.out.println();
        System.out.println("=== Undo History ===");
        remote.printHistory();

        System.out.println("\n=== Undoing Last Command ===");
        remote.undo();

        System.out.println("\n=== Undoing Another Command ===");
        remote.undo();

        System.out.println();
        remote.printHistory();
    }
}