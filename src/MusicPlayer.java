import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

// plays background music using Java sound, so it works on Windows and Mac
// before i used afplay and i realized it only worked on mac, so now we used clip so it works on both instances
public class MusicPlayer {

    private static Clip currentClip = null;

    // starts looping one music file
    public static void startLoop(File musicFile) {
        stop();

        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(musicFile);
            currentClip = AudioSystem.getClip();
            currentClip.open(audio);
            currentClip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            currentClip = null;
        }
    }

    // stops the current music
    public static void stop() {
        if (currentClip != null) {
            currentClip.stop();
            currentClip.close();
            currentClip = null;
        }
    }

    // main menu music
    // we have a wav for the main menu (startpanel) and one music for the actual game
    public static File findThemeFile() {
        return new File("assets/audio/theme.wav");
    }

    // main match music
    public static File findBattleMusicFile() {
        return new File("assets/audio/coconut-mall-battle-music.wav");
    }
}
