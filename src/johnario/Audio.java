package johnario;

import java.io.InputStream;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 * Kniznica potrebna pre zvul
 * @author pego1
 */
public class Audio {
    private AudioStream audioStream;

    /**
     * Konstruktor
     * @param s cesta
     */
    public Audio(String s) {
        try {
            InputStream inputStream = getClass().getResourceAsStream(s);
            this.audioStream = new AudioStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Hraj
     */
    public void play() {
        AudioPlayer.player.start(this.audioStream);
    }

    /**
     * Zastav
     */
    public void stop() {
        AudioPlayer.player.stop(this.audioStream);
    }
}