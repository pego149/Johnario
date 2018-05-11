package johnario;

import java.io.InputStream;
import javax.sound.sampled.*;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class Audio {
    private AudioStream audioStream;
    public Audio(String s) {
        try {
            InputStream inputStream = getClass().getResourceAsStream(s);
            audioStream = new AudioStream(inputStream);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        AudioPlayer.player.start(audioStream);
    }

    public void stop() {
        AudioPlayer.player.stop(audioStream);
    }
}