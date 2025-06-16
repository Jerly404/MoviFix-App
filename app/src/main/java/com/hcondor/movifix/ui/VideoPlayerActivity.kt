package com.hcondor.movifix

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.datasource.okhttp.OkHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.PlayerView
import com.hcondor.movifix.utils.InsecureSSLSocketFactory
import kotlinx.coroutines.*

class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var player: ExoPlayer
    private lateinit var playerView: PlayerView
    private lateinit var btnFullscreen: ImageButton
    private lateinit var tvDescription: TextView

    private var isFullscreen = false
    private var playerInitialized = false

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        playerView = findViewById(R.id.playerView)
        btnFullscreen = findViewById(R.id.btnFullscreen)
        tvDescription = findViewById(R.id.tvDescription)

        val videoUrl = intent.getStringExtra("videoUrl") ?: ""
        val description = intent.getStringExtra("description") ?: "Sin descripción"
        tvDescription.text = description

        btnFullscreen.setOnClickListener {
            toggleFullscreen()
        }

        scope.launch {
            val isValid = withContext(Dispatchers.IO) { validateVideoUrl(videoUrl) }
            if (isValid) {
                initializePlayer(videoUrl)
            } else {
                Toast.makeText(this@VideoPlayerActivity, "Video no disponible", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun toggleFullscreen() {
        isFullscreen = !isFullscreen

        requestedOrientation = if (isFullscreen) {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }

        // Ocultar o mostrar UI según el modo
        window.decorView.systemUiVisibility = if (isFullscreen) {
            (View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        } else {
            View.SYSTEM_UI_FLAG_VISIBLE
        }

        // Opcional: ocultar también la descripción si estás en fullscreen
        tvDescription.visibility = if (isFullscreen) View.GONE else View.VISIBLE
    }

    @OptIn(androidx.media3.common.util.UnstableApi::class)
    private fun initializePlayer(videoUrl: String) {
        player = ExoPlayer.Builder(this).build()
        playerView.player = player

        val okHttpClient = okhttp3.OkHttpClient.Builder()
            .sslSocketFactory(
                InsecureSSLSocketFactory.sslContext.socketFactory,
                InsecureSSLSocketFactory.trustManager
            )
            .hostnameVerifier { _, _ -> true }
            .build()

        val dataSourceFactory = OkHttpDataSource.Factory(okHttpClient)

        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Uri.parse(videoUrl)))

        player.setMediaSource(mediaSource)
        player.prepare()
        player.playWhenReady = true
        playerInitialized = true
    }

    private suspend fun validateVideoUrl(url: String): Boolean {
        return url.isNotBlank() // Valida que no esté vacío
    }

    override fun onBackPressed() {
        if (isFullscreen) {
            toggleFullscreen()
        } else {
            super.onBackPressed()
        }
    }

    override fun onStop() {
        super.onStop()
        if (playerInitialized) {
            player.release()
            playerView.player = null
            playerInitialized = false
        }
        scope.cancel()
    }
}
