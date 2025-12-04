package org.java.design.pattern.behavioral.state;

// State interface - defines contract for all states
interface State {
    void play(MediaPlayer player);

    void pause(MediaPlayer player);

    void stop(MediaPlayer player);
}

// Concrete states
class StoppedState implements State {
    @Override
    public void play(MediaPlayer player) {
        System.out.println("Playing...");
        player.setState(new PlayingState());
    }

    @Override
    public void pause(MediaPlayer player) {
        System.out.println("Cannot pause when stopped!");
    }

    @Override
    public void stop(MediaPlayer player) {
        System.out.println("Already stopped!");
    }
}

class PlayingState implements State {
    @Override
    public void play(MediaPlayer player) {
        System.out.println("Already playing!");
    }

    @Override
    public void pause(MediaPlayer player) {
        System.out.println("Pausing...");
        player.setState(new PausedState());
    }

    @Override
    public void stop(MediaPlayer player) {
        System.out.println("Stopping...");
        player.setState(new StoppedState());
    }
}

class PausedState implements State {
    @Override
    public void play(MediaPlayer player) {
        System.out.println("Resuming...");
        player.setState(new PlayingState());
    }

    @Override
    public void pause(MediaPlayer player) {
        System.out.println("Already paused!");
    }

    @Override
    public void stop(MediaPlayer player) {
        System.out.println("Stopping...");
        player.setState(new StoppedState());
    }
}

// Context - the object whose behavior changes
class MediaPlayer {
    private State currentState;

    public MediaPlayer() {
        this.currentState = new StoppedState();
    }

    public void setState(State state) {
        this.currentState = state;
    }

    public void play() {
        currentState.play(this);
    }

    public void pause() {
        currentState.pause(this);
    }

    public void stop() {
        currentState.stop(this);
    }

    public String getCurrentState() {
        return currentState.getClass().getSimpleName();
    }
}

/**
 * * State Pattern Demo
 * * This demo showcases a media player that changes its behavior based on its current state
 * * (Stopped, Playing, Paused). The media player responds to user actions like play
 * * pause and stop, transitioning between states accordingly.
 */
public class StatePatternDemo {
    public static void main(String[] args) {
        MediaPlayer player = new MediaPlayer();

        System.out.println("Current state: " + player.getCurrentState());

        System.out.println("\n=== User Actions ===");
        player.play();      // Stopped -> Playing
        System.out.println("Current state: " + player.getCurrentState());

        System.out.println();
        player.play();      // Already playing

        System.out.println();
        player.pause();     // Playing -> Paused
        System.out.println("Current state: " + player.getCurrentState());

        System.out.println();
        player.play();      // Paused -> Playing
        System.out.println("Current state: " + player.getCurrentState());

        System.out.println();
        player.stop();      // Playing -> Stopped
        System.out.println("Current state: " + player.getCurrentState());

        System.out.println();
        player.pause();     // Cannot pause when stopped

        System.out.println();
        player.play();      // Stopped -> Playing
        System.out.println("Current state: " + player.getCurrentState());
    }
}