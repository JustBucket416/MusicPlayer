package justbucket.musicplayer.example

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import justbucket.musicplayer.MusicEntity
import justbucket.musicplayer.MusicPlayerCallback
import justbucket.musicplayer.MusicPlayerLauncher
import justbucket.musicplayer.MusicPlayerLauncher.STATE_BOUND
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val playerLauncher = MusicPlayerLauncher()

    private val playerCallback = object : MusicPlayerCallback {
        override fun play() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun pause() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun stop() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun next() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun previous() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun seekTo(millis: Long) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getProgressMillis(): Long {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getTotalDuarionMillis(): Long {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler.layoutManager = androidx.recyclerview.widget.GridLayoutManager(this, 2)
        recycler.adapter = VideoAdapter(createVideoList()) {
            videoView.setVideoURI(Uri.parse(it))
            videoView.start()
            videoView.setVolume(0)

            playerLauncher.startPlayer(
                this,
                arrayListOf(MusicEntity.Builder().setTitle(it).buildEntity()),
                playerCallback,
                STATE_BOUND
            )
        }


        videoView.setOnTouchListener { _, _ ->
            videoView.pause()
            true
        }
    }

    private fun createVideoList() =
        arrayListOf(
            "android.resource://$packageName/${R.raw.videoa}",
            "android.resource://$packageName/${R.raw.videob}",
            "android.resource://$packageName/${R.raw.videoc}",
            "android.resource://$packageName/${R.raw.videod}",
            "android.resource://$packageName/${R.raw.videoe}",
            "android.resource://$packageName/${R.raw.videof}"
        )

    private fun VideoView.setVolume(amount: Int) {
        val max = 100
        val numerator: Double = if (max - amount > 0) Math.log((max - amount).toDouble()) else 0.0
        val volume = (1 - numerator / Math.log(max.toDouble())).toFloat()
        val field = this::class.java.getDeclaredField("mMediaPlayer")
        field.isAccessible = true
        (field.get(this) as MediaPlayer).setVolume(volume, volume)
        field.isAccessible = false
    }
}
