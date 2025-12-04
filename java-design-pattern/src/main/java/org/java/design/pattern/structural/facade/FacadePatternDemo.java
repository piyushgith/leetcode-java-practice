package org.java.design.pattern.structural.facade;

// Complex subsystem classes - each handles one piece
class CPU {
    public void start() {
        System.out.println("CPU starting...");
    }

    public void execute() {
        System.out.println("CPU executing...");
    }

    public void stop() {
        System.out.println("CPU stopping...");
    }
}

class Memory {
    public void load() {
        System.out.println("Memory loading...");
    }

    public void clear() {
        System.out.println("Memory clearing...");
    }
}

class HardDrive {
    public void read() {
        System.out.println("Hard drive reading data...");
    }

    public void write() {
        System.out.println("Hard drive writing data...");
    }
}

class GPU {
    public void initialize() {
        System.out.println("GPU initializing...");
    }

    public void render() {
        System.out.println("GPU rendering graphics...");
    }

    public void shutdown() {
        System.out.println("GPU shutting down...");
    }
}

// Facade - provides simple interface to complex subsystem
class ComputerFacade {
    private CPU cpu;
    private Memory memory;
    private HardDrive hardDrive;
    private GPU gpu;

    public ComputerFacade() {
        this.cpu = new CPU();
        this.memory = new Memory();
        this.hardDrive = new HardDrive();
        this.gpu = new GPU();
    }

    // Simple operation - hides complexity
    public void startup() {
        System.out.println("=== Computer Starting ===");
        cpu.start();
        memory.load();
        hardDrive.read();
        gpu.initialize();
        System.out.println("Computer ready!\n");
    }

    public void playGame() {
        System.out.println("=== Playing Game ===");
        cpu.execute();
        gpu.render();
        System.out.println("Game running!\n");
    }

    public void shutdown() {
        System.out.println("=== Computer Shutting Down ===");
        gpu.shutdown();
        cpu.stop();
        memory.clear();
        hardDrive.write();
        System.out.println("Computer off!\n");
    }
}

/**
 * Facade Pattern Demo
 * In this example, the ComputerFacade class provides a simplified interface
 * to a complex subsystem consisting of CPU, Memory, HardDrive, and GPU classes.
 * The client code interacts with the facade to start up the computer, play a game,
 * and shut down the computer without needing to understand the complexities of
 * the underlying components.
 */
public class FacadePatternDemo {
    public static void main(String[] args) {
        // With facade, client just does this:
        ComputerFacade computer = new ComputerFacade();
        computer.startup();
        computer.playGame();
        computer.shutdown();

        // Without facade, client would need to do this:
        /*
        CPU cpu = new CPU();
        Memory memory = new Memory();
        HardDrive hardDrive = new HardDrive();
        GPU gpu = new GPU();

        cpu.start();
        memory.load();
        hardDrive.read();
        gpu.initialize();
        */
    }
}
