package PaooGame.Audio;

import javax.sound.sampled.*;
import java.io.File;

public class AudioPlayer {
    private static Clip bgmClip;

    public static void playSound(String path) {
        try {
            File audioFile = new File(path);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            System.err.println("Eroare la redarea sunetului: " + path);
        }
    }
}
