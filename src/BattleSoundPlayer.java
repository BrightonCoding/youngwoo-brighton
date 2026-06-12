import java.io.File;

// plays the item-box sound on a loop during the candy battle
public class BattleSoundPlayer {

    private static final String SOUND_FILE = "assets/audio/item-box-camera-sound.mp3";

    private static Process currentSound = null;
    private static boolean keepPlaying = false;

    /**
     * pre:  the item-box sound file exists in assets/audio
     * post: the item-box sound starts looping in the background until stop() is called
     */
    public static void startLoop() {
        stop();

        File soundFile = new File(SOUND_FILE);
        if (!soundFile.exists()) {
            return;
        }

        keepPlaying = true;

        Thread soundThread = new Thread(() -> {
            while (keepPlaying) {
                try {
                    currentSound = new ProcessBuilder("afplay", soundFile.getPath()).start();
                    currentSound.waitFor();
                } catch (Exception e) {
                    keepPlaying = false;
                }
            }
        });
        soundThread.setDaemon(true);
        soundThread.start();
    }

    /**
     * pre:  none
     * post: the item-box battle sound stops immediately
     */
    public static void stop() {
        keepPlaying = false;
        if (currentSound != null) {
            currentSound.destroy();
            currentSound = null;
        }
    }
}
