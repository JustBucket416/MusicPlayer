package justbucket.musicplayer.example

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide

class VideoAdapter(private val videos: List<String>, private val onClick: (String) -> Unit) :
    androidx.recyclerview.widget.RecyclerView.Adapter<VideoAdapter.VideoHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): VideoHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.video_holder, p0, false)
        return VideoHolder(view)
    }

    override fun getItemCount() = videos.size

    override fun onBindViewHolder(p0: VideoHolder, p1: Int) {
        p0.bind(videos[p1], onClick)
    }

    class VideoHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bind(s: String, onClick: (String) -> Unit) {
            Glide.with(itemView.context)
                .load(s)
                .into(itemView as ImageView)

            itemView.setOnClickListener { onClick(s) }
        }
    }
}