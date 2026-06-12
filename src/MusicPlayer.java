import java.io.File;

// plays background music on a loop using macOS's afplay command
public class MusicPlayer {

    private static Process currentSound = null;
    private static boolean playing = false;
    private static String volume = "1.00";

    /**
     * pre:  none
     * post: the given music file starts looping in the background until stop()
     *       or another track is started; any previous music is stopped
     */
    public static void startLoop(File musicFile) {
        stop();

        if (musicFile == null || !musicFile.exists()) {
            return;
        }

        final String path = musicFile.getPath();
        volume = "1.00";
        playing = true;

        Thread musicThread = new Thread(() -> {
            while (playing) {
                try {
                    currentSound = new ProcessBuilder("afplay", "-v", volume, path).start();
                    currentSound.waitFor();
                } catch (Exception e) {
                    // music's not important enough to crash over, so just stop trying
                    playing = false;
                }
            }
        });
        musicThread.setDaemon(true);
        musicThread.start();
    }

    /**
     * pre:  none
     * post: the music keeps playing, but becomes quieter the next time it loops
     */
    public static void lowerVolume() {
        volume = "0.20";
    }

    /**
     * pre:  none
     * post: the music stops immediately
     */
    public static void stop() {
        playing = false;
        if (currentSound != null) {
            currentSound.destroy();
            currentSound = null;
        }
    }

    /**
     * pre:  none
     * post: returns the theme music file in assets/audio
     */
    public static File findThemeFile() {
        return new File("assets/audio/theme.mp3");
    }

    /**
     * pre:  fileName includes the extension (e.g. "match-music.mp3")
     * post: returns the file with that name in assets/audio (it may or may not exist)
     */
    public static File findAudio(String fileName) {
        return new File("assets/audio/" + fileName);
    }

    /**
     * pre:  none
     * post: returns the battle music file in assets/audio, or null if it does not exist
     */
    public static File findBattleMusicFile() {
        File battleMusic = new File("assets/audio/coconut-mall-battle-music.mp3");
        if (battleMusic.exists()) {
            return battleMusic;
        }
        return null;
    }
}
