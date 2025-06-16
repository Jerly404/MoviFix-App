package com.hcondor.movifix.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.hcondor.movifix.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val logo = findViewById<ImageView>(R.id.logoImage)

        val rotateAnim = ObjectAnimator.ofFloat(logo, "rotation", 0f, 1800f)
        rotateAnim.duration = 5000
        rotateAnim.interpolator = DecelerateInterpolator()

        rotateAnim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                // Redirige a Login/Register al terminar
                startActivity(Intent(this@SplashActivity, WelcomeActivity::class.java))
                finish()
            }   
        })

        rotateAnim.start()
    }
}