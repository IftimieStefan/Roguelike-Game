package PaooGame.Audio;

import javax.sound.sampled.*;
import java.io.File;

public class AudioPlayer {
    private static Clip bgmClip;

    //volum intre 0 -> 1
    private static float volume = 0.8f;

    public static float getVolume() {
        return volume;
    }

    public static void setVolume(float v) {
        volume = Math.max(0f, Math.min(1f, v));
    }

    public static void playSound(String path) {
        try {
            File audioFile = new File(path);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            applyVolume(clip);
            clip.start();
        } catch (Exception e) {
            System.err.println("Eroare la redarea sunetului: " + path);
        }
    }

    private static void applyVolume(Clip clip) {
        try {
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                // Convertim 0..1 in decibeli 
                float dB;
                if (volume <= 0f) {
                    dB = gainControl.getMinimum();
                } else {
                    dB = 20f * (float) Math.log10(volume);
                    dB = Math.max(dB, gainControl.getMinimum());
                    dB = Math.min(dB, gainControl.getMaximum());
                }
                gainControl.setValue(dB);
            }
        } catch (Exception ignored) {
        }
    }
}
