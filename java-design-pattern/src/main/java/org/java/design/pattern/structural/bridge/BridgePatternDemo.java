package org.java.design.pattern.structural.bridge;

// Implementer interface - abstraction for implementations
interface Device {
    void turnOn();

    void turnOff();

    void setVolume(int volume);
}

// Concrete implementers
class TV implements Device {
    private boolean isOn = false;
    private int volume = 0;

    @Override
    public void turnOn() {
        isOn = true;
        System.out.println("TV is ON");
    }

    @Override
    public void turnOff() {
        isOn = false;
        System.out.println("TV is OFF");
    }

    @Override
    public void setVolume(int volume) {
        this.volume = volume;
        System.out.println("TV volume set to " + volume);
    }
}

class Radio implements Device {
    private boolean isOn = false;
    private int volume = 0;

    @Override
    public void turnOn() {
        isOn = true;
        System.out.println("Radio is ON");
    }

    @Override
    public void turnOff() {
        isOn = false;
        System.out.println("Radio is OFF");
    }

    @Override
    public void setVolume(int volume) {
        this.volume = volume;
        System.out.println("Radio volume set to " + volume);
    }
}

// Abstraction - uses the device interface
abstract class Remote {
    protected Device device; // Bridge to implementation

    public Remote(Device device) {
        this.device = device;
    }

    abstract void powerOn();

    abstract void powerOff();

    abstract void volumeUp();

    abstract void volumeDown();
}

// Refined abstractions
class BasicRemote extends Remote {
    public BasicRemote(Device device) {
        super(device);
    }

    @Override
    public void powerOn() {
        device.turnOn();
    }

    @Override
    public void powerOff() {
        device.turnOff();
    }

    @Override
    public void volumeUp() {
        device.setVolume(50);
    }

    @Override
    public void volumeDown() {
        device.setVolume(10);
    }
}

class AdvancedRemote extends Remote {
    public AdvancedRemote(Device device) {
        super(device);
    }

    @Override
    public void powerOn() {
        device.turnOn();
    }

    @Override
    public void powerOff() {
        device.turnOff();
    }

    @Override
    public void volumeUp() {
        device.setVolume(100);
    }

    @Override
    public void volumeDown() {
        device.setVolume(0);
    }

    public void mute() {
        device.setVolume(0);
        System.out.println("Device muted");
    }
}

/**
 * Bridge Pattern Demo
 * This demo shows how to separate an abstraction (Remote) from its implementation (Device)
 * so that the two can vary independently.
 */
public class BridgePatternDemo {
    public static void main(String[] args) {
        // Create devices
        Device tv = new TV();
        Device radio = new Radio();

        // Bridge: Connect remotes to devices
        // Basic remote can work with any device
        Remote basicRemote = new BasicRemote(tv);
        basicRemote.powerOn();
        basicRemote.volumeUp();
        basicRemote.powerOff();

        System.out.println();

        // Advanced remote with same device
        Remote advancedRemote = new AdvancedRemote(tv);
        advancedRemote.powerOn();
        advancedRemote.volumeUp();
        advancedRemote.volumeDown();
        ((AdvancedRemote) advancedRemote).mute();
        advancedRemote.powerOff();

        System.out.println();

        // Same remote type with different device
        Remote basicRadioRemote = new BasicRemote(radio);
        basicRadioRemote.powerOn();
        basicRadioRemote.volumeUp();
        basicRadioRemote.powerOff();
    }
}
