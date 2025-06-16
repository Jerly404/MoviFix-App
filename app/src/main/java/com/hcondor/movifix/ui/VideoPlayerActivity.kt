package com.hcondor.movifix.ui

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.hcondor.movifix.R

class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var player: ExoPlayer
    private lateinit var playerView: PlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        playerView = findViewById(R.id.playerView)
        player = ExoPlayer.Builder(this).build()
        playerView.player = player

        val videoUrl = intent.getStringExtra("videoUrl") ?: return
        val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
        player.setMediaItem(mediaItem)

        player.prepare()
        player.playWhenReady = true
    }

    override fun onStop() {
        super.onStop()
        player.release()
    }
}
