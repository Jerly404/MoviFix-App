package com.hcondor.movifix

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.HttpDataSource
import androidx.media3.datasource.okhttp.OkHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.ui.PlayerView
import com.hcondor.movifix.utils.InsecureSSLSocketFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@UnstableApi
class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var player: ExoPlayer
    private lateinit var playerView: PlayerView
    private lateinit var btnFullscreen: ImageButton
    private lateinit var tvDescription: TextView

    private var isFullscreen = false
    private var playerInitialized = false // Para mayor seguridad

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

        // Validar la URL con GET (puedes desactivar validación si solo es prueba)
        CoroutineScope(Dispatchers.IO).launch {
            if (validateVideoUrl(videoUrl)) {
                withContext(Dispatchers.Main) {
                    initializePlayer(videoUrl)
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@VideoPlayerActivity, "Video no disponible", Toast.LENGTH_SHORT).show()
                    finish()
                }
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
    }

    private fun initializePlayer(videoUrl: String) {
        player = ExoPlayer.Builder(this).build()
        playerView.player = player

        val okHttpClient = okhttp3.OkHttpClient.Builder()
            .sslSocketFactory(InsecureSSLSocketFactory.sslContext.socketFactory, InsecureSSLSocketFactory.trustManager)
            .hostnameVerifier { _, _ -> true } // Acepta cualquier nombre de host
            .build()

        val dataSourceFactory = OkHttpDataSource.Factory(okHttpClient)

        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Uri.parse(videoUrl)))

        player.setMediaSource(mediaSource)
        player.prepare()
        player.playWhenReady = true
    }


    // ⚠️ Para pruebas: permite videos sin HTTPS válidos ni certificados
    private suspend fun validateVideoUrl(url: String): Boolean {
        return true
    }

    override fun onStop() {
        super.onStop()
        if (playerInitialized) {
            player.release()
            playerView.player = null
            playerInitialized = false
        }
    }
}
