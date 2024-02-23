package app.music

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import app.music.databinding.ActivityPlayerBinding
import com.bumptech.glide.Glide

class PlayerActivity : AppCompatActivity() {

    lateinit var binding: ActivityPlayerBinding
    lateinit var exoPlayer: ExoPlayer

    var playerListener = object : Player.Listener{
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            showGif(isPlaying)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MyExoPlayer.getCurrentSong()?.apply {
            binding.songTitleTextview.text = title
            binding.songSubtitleTextview.text = subtitle
            Glide.with(binding.songCoverImageview).load(coverUrl)
                .circleCrop()
                .into(binding.songCoverImageview)
            Glide.with(binding.songGifImageview).load(R.drawable.media_playing)
                .circleCrop()
                .into(binding.songGifImageview)

            exoPlayer = MyExoPlayer.getInstance()!!
            binding.playerView.player = exoPlayer
            binding.playerView.showController()
            exoPlayer.addListener(playerListener)
        }
    }

    fun showGif(show: Boolean){
        if(show){
            binding.songGifImageview.visibility = View.VISIBLE
        }else{
            binding.songGifImageview.visibility = View.INVISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.removeListener(playerListener)
    }
}