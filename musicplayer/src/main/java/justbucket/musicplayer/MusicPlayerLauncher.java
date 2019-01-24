package justbucket.musicplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import justbucket.musicplayer.service.MusicPlayerService;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayerLauncher {

    @IntDef({STATE_BOUND, STATE_UNBOUND})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MusicPlayerServiceState{}

    public static final int STATE_UNBOUND = 0;

    public static final int STATE_BOUND = 1;

    private int position = 0;

    private double duration = 0d;

    private MusicPlayerService mService;

    private MusicPlayerCallback mCallback;

    private MusicEntity mEntity;

    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = ((MusicPlayerService.LocalBinder) service).getService();
            mService.setPlayerCallback(mCallback);
            mService.playMusic(mEntity);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };

    public void startPlayer(@NonNull Context context,
                            @NonNull ArrayList<MusicEntity> musicList,
                            @NonNull MusicPlayerCallback callback,
                            @MusicPlayerServiceState int state) {
        mCallback = callback;
        if (state==STATE_UNBOUND) {
            context.startService(MusicPlayerService.newUnboundIntent(context, musicList, position));
        } else {
            mEntity = musicList.get(0);
            context.bindService(MusicPlayerService.newBoundIntent(context), mConnection, Context.BIND_AUTO_CREATE);
        }
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
