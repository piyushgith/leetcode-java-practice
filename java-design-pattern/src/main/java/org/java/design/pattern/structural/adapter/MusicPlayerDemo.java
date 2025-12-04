package org.java.design.pattern.structural.adapter;


interface MediaPlayer {
    void play(String audioType, String fileName);
}

class VlcPlayer {
    public void playVlc(String fileName) {
        System.out.println("Playing VLC file: " + fileName);
    }
}

class VlcAdapter implements MediaPlayer {
    private VlcPlayer vlcPlayer;

    public VlcAdapter() {
        vlcPlayer = new VlcPlayer();
    }

    public void play(String audioType, String fileName) {
        if ("vlc".equalsIgnoreCase(audioType)) {
            vlcPlayer.playVlc(fileName);
        }
    }
}

class AudioPlayer implements MediaPlayer {
    public void play(String audioType, String fileName) {
        if ("mp3".equalsIgnoreCase(audioType)) {
            System.out.println("Playing MP3 file: " + fileName);
        } else if ("vlc".equalsIgnoreCase(audioType)) {
            //file is vlc type, use adapter
            MediaPlayer adapter = new VlcAdapter();
            adapter.play(audioType, fileName);
        } else {
            System.out.println("Format not supported");
        }
    }
}

public class MusicPlayerDemo {
    public static void main(String[] args) {
        AudioPlayer player = new AudioPlayer();
        player.play("mp3", "song.mp3");
        player.play("vlc", "video.vlc");
    }
}