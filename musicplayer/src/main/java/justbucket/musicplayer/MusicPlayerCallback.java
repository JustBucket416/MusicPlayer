package justbucket.musicplayer;

public interface MusicPlayerCallback {

    void play();

    void pause();

    void stop();

    void next();

    void previous();

    void seekTo(long millis);

    long getProgressMillis();

    long getTotalDuarionMillis();
}
