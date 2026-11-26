package group19.project_cs230;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Plays background music and sound effects for the game.
 */
public class AudioManager {

    private static MediaPlayer bgmPlayer;
    private static final Map<String, AudioClip> sfxCache = new HashMap<>();

    /**
     * Plays a short sound effect. Clips are cached so they only load once.
     *
     * @param path resource path, e.g. "/audio/click.wav"
     */
    public static void playSFX(String path) {
        AudioClip clip = sfxCache.computeIfAbsent(path, p -> {
            String url = urlOf(p);
            return url == null ? null : new AudioClip(url);
        });
        if (clip != null) clip.play();
    }

    /**
     * Loops background music, replacing any music already playing.
     *
     * @param path resource path, e.g. "/audio/menu_music.mp3"
     */
    public static void playMusic(String path) {
        if (bgmPlayer != null) {
            bgmPlayer.dispose(); // also stops playback
        }
        String url = urlOf(path);
        if (url == null) return;

        bgmPlayer = new MediaPlayer(new Media(url));
        bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        bgmPlayer.setVolume(0.5);
        bgmPlayer.play();
    }

    /**
     * Finds a resource and returns its URL as a string.
     *
     * @param path resource path
     * @return the URL string, or null if the file is not found
     */
    private static String urlOf(String path) {
        URL url = AudioManager.class.getResource(path);
        if (url == null) {
            System.err.println("Audio file not found: " + path);
        }
        return url == null ? null : url.toExternalForm();
    }
}