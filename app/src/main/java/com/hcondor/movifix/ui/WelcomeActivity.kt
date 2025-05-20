package com.hcondor.movifix.ui

import ZoomOutPageTransformer
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.hcondor.movifix.R
import android.os.Handler
import android.os.Looper
import java.util.*

class WelcomeActivity : AppCompatActivity() {

    private lateinit var indicatorLayout: LinearLayout
    private lateinit var sliderAdapter: SliderAdapter
    private lateinit var imageSlider: ViewPager2
    private val imageList = listOf(
        R.drawable.welcome_image1,
        R.drawable.welcome_image2,
        R.drawable.welcome_image3,
        R.drawable.welcome_image4,
        R.drawable.welcome_image5,
        R.drawable.welcome_image6,
        R.drawable.welcome_image7,
        R.drawable.welcome_image8
    )

    private val sliderHandler = Handler(Looper.getMainLooper())
    private var sliderRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        imageSlider = findViewById(R.id.imageSlider)
        indicatorLayout = findViewById(R.id.indicatorLayout)

        sliderAdapter = SliderAdapter(imageList)
        imageSlider.adapter = sliderAdapter

        imageSlider.setPageTransformer(ZoomOutPageTransformer())

        setupIndicators()
        setCurrentIndicator(0)

        imageSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                setCurrentIndicator(position)
            }
        })
        sliderRunnable = Runnable {
            val nextItem = (imageSlider.currentItem + 1) % imageList.size
            imageSlider.currentItem = nextItem
        }

        // Repetimos cada 3 segundos
        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    sliderRunnable?.run()
                }
            }
        }, 3000, 3000)

        val btnLoginRegister = findViewById<Button>(R.id.btnLoginRegister)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLoginRegister.setOnClickListener {
            btnLoginRegister.isSelected = true
            btnLogin.isSelected = false
            startActivity(Intent(this, LoginChoiceActivity::class.java))
        }

        btnLogin.setOnClickListener {
            btnLogin.isSelected = true
            btnLoginRegister.isSelected = false
            startActivity(Intent(this, LoginActivity::class.java))
        }

    }

    private fun setupIndicators() {
        val indicators = arrayOfNulls<ImageView>(imageList.size)
        val layoutParams = LinearLayout.LayoutParams(24, 8)
        layoutParams.setMargins(8, 0, 8, 0)

        for (i in indicators.indices) {
            indicators[i] = ImageView(this).apply {
                setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.indicator_inactive
                    )
                )
                this.layoutParams = layoutParams
            }
            indicatorLayout.addView(indicators[i])
        }
    }

    private fun setCurrentIndicator(index: Int) {
        val count = indicatorLayout.childCount
        for (i in 0 until count) {
            val imageView = indicatorLayout.getChildAt(i) as ImageView
            imageView.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    if (i == index) R.drawable.indicator_active else R.drawable.indicator_inactive
                )
            )
        }
    }
}
