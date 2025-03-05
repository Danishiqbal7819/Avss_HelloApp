package com.example.helloapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.airbnb.lottie.LottieAnimationView
import com.example.helloapp.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.buttonStart.setOnClickListener {
            Toast.makeText(this, "Started", Toast.LENGTH_LONG).show()
//            startService(Intent(this, MusicService::class.java))
            startAndStop("play")
        }

        binding.buttonStop.setOnClickListener {
            Toast.makeText(this, "Stopped", Toast.LENGTH_LONG).show()
//            stopService(Intent(this, MusicService::class.java))
            startAndStop("pause")
        }
    }

    fun startAndStop(i: String) {
        try {
            val animationView = findViewById<LottieAnimationView>(R.id.animation_view)
            val animationView2 = findViewById<LottieAnimationView>(R.id.animation_view2)
            animationView.setAnimation(R.raw.anim)
            animationView2.setAnimation(R.raw.astronot)
            if (i.equals("play")) {
                animationView.playAnimation()
                animationView2.playAnimation()
            } else if (i.equals("pause")) {
                animationView.pauseAnimation()
                animationView2.pauseAnimation()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "$e", Toast.LENGTH_LONG).show()
        }
    }
}