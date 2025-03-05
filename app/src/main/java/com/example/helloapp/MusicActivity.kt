package com.example.helloapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.helloapp.databinding.ActivityMusicBinding

class MusicActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMusicBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMusicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setContentView(R.layout.activity_music)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main1)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.buttonStart.setOnClickListener {
            Toast.makeText(this, "Started", Toast.LENGTH_LONG).show()
            startService(Intent(this, MusicService::class.java))
        }

        binding.buttonStop.setOnClickListener {
            Toast.makeText(this, "Stopped", Toast.LENGTH_LONG).show()
            stopService(Intent(this, MusicService::class.java))
        }
    }
}