package justbucket.musicplayer.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Binder
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v4.media.session.PlaybackStateCompat.STATE_PLAYING
import android.view.KeyEvent
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import justbucket.musicplayer.MusicEntity
import justbucket.musicplayer.MusicPlayerCallback

class MusicPlayerService : Service() {

    private val mBinder = LocalBinder()
    private lateinit var requestManager: RequestManager
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var eventReceiver: ComponentName
    private lateinit var notificationManager: NotificationManager
    private var mPlayerCallback: MusicPlayerCallback? = null


    override fun onCreate() {
        super.onCreate()
        requestManager = Glide.with(this)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onBind(intent: Intent?) = mBinder

    @JvmName("playMusic")
    internal fun playMusic(entity: MusicEntity) {
        if (!this::mediaSession.isInitialized) {
            createMediaSession()
        }
        sendState(entity, STATE_PLAYING, 0L)
    }

    @JvmName("setPlayerCallback")
    internal fun setPlayerCallback(callback: MusicPlayerCallback) {
        mPlayerCallback = callback
    }

    private fun createMediaSession() {
        mediaSession = MediaSessionCompat(this, SERVICE_NAME)
        mediaSession.setCallback(object : MediaSessionCompat.Callback() {

        })

        mediaSession.isActive = true
    }

    private fun sendState(entity: MusicEntity, state: Int, position: Long) {
        mediaSession.setPlaybackState(
            PlaybackStateCompat.Builder()
                .setState(state, position, 1f)
                .build()
        )

        requestManager.asBitmap().load(entity.albumArtPath).into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                mediaSession.setMetadata(
                    MediaMetadataCompat.Builder()
                        .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, resource)
                        .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, entity.album)
                        .putString(MediaMetadataCompat.METADATA_KEY_TITLE, entity.title)
                        .build()
                )
            }
        })

        notificationManager.notify(NOTIFICATION_ID, buildNotification(entity))
    }


    private fun buildNotification(entity: MusicEntity) =
        with(entity) {
            val builder = NotificationCompat.Builder(this@MusicPlayerService, SERVICE_NAME)


            return@with builder.build()
        }

    private fun buildMediaButtonIntent(keycode: Int, context: Context): PendingIntent {
        val intent = Intent(Intent.ACTION_MEDIA_BUTTON)
        intent.component = eventReceiver
        intent.putExtra(Intent.EXTRA_KEY_EVENT, KeyEvent(KeyEvent.ACTION_DOWN, keycode))
        return PendingIntent.getBroadcast(context, keycode, intent, 0)
    }

    inner class LocalBinder : Binder() {
        @JvmName("getService")
        internal fun getService() = this@MusicPlayerService
    }

    companion object {

        private const val AUDIO_LIST_KEY = "audio-list-key"
        private const val POSITION_KEY = "position-key"
        private const val SERVICE_NAME = "MusicPlayerService"
        private const val NOTIFICATION_ID = 416

        @JvmStatic
        fun newUnboundIntent(context: Context?, audioList: ArrayList<MusicEntity>, position: Int) =
            Intent(context, MusicPlayerService::class.java).apply {
                putParcelableArrayListExtra(AUDIO_LIST_KEY, audioList)
                putExtra(POSITION_KEY, position)
            }

        @JvmStatic
        fun newBoundIntent(context: Context?) = Intent(context, MusicPlayerService::class.java)

    }
}