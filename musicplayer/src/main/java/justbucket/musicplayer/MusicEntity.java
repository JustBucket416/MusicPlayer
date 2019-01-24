package justbucket.musicplayer;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class MusicEntity implements Parcelable {

    public static final Creator<MusicEntity> CREATOR = new Creator<MusicEntity>() {
        @Override
        public MusicEntity createFromParcel(Parcel in) {
            return new MusicEntity(in);
        }

        @Override
        public MusicEntity[] newArray(int size) {
            return new MusicEntity[size];
        }
    };
    private static final String TITLE = "Title";

    @NonNull
    private final String title;
    @Nullable
    private final String album;
    @Nullable
    private final String artist;
    @Nullable
    private final String albumArtPath;

    private MusicEntity(@NonNull String title, @Nullable String album, @Nullable String artist, @Nullable String albumArtPath) {
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.albumArtPath = albumArtPath;
    }

    protected MusicEntity(Parcel in) {
        String s = in.readString();
        title = s != null ? s : TITLE;
        album = in.readString();
        artist = in.readString();
        albumArtPath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(album);
        dest.writeString(artist);
        dest.writeString(albumArtPath);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getAlbum() {
        return album;
    }

    @Nullable
    public String getArtist() {
        return artist;
    }

    @Nullable
    public String getAlbumArtPath() {
        return albumArtPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MusicEntity that = (MusicEntity) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(album, that.album) &&
                Objects.equals(artist, that.artist) &&
                Objects.equals(albumArtPath, that.albumArtPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, album, artist, albumArtPath);
    }

    public static class Builder {
        private String title;
        private String album;
        private String artist;
        private String albumArtPath;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setAlbum(String album) {
            this.album = album;
            return this;
        }

        public Builder setArtist(String artist) {
            this.artist = artist;
            return this;
        }

        public Builder setAlbumArtPath(String albumArtPath) {
            this.albumArtPath = albumArtPath;
            return this;
        }

        public MusicEntity buildEntity() {
            return new MusicEntity(title, album, artist, albumArtPath);
        }
    }
}
