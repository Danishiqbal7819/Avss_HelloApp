package com.example.helloapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.helloapp.databinding.ActivityNotificationBinding

class NotificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationBinding
    private var CHANNEL_ID = "MyChannel"
    private var notification_ID = 101
    private var notification: Notification? = null
    private lateinit var nm: NotificationManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager


        binding.sendNotificationButton.setOnClickListener {
            Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show()
            notificationFun()
        }
    }

    private fun notificationFun() {
        val largeIconBitmap = BitmapFactory.decodeResource(resources, R.drawable.hello)
        val intent = Intent(this, MainActivity2::class.java).apply {
            intent.putExtra("val1", "Hellooo")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = Notification.Builder(applicationContext)
                .setContentTitle("Someone")
                .setContentText("Hello Danish,\nits time to be productive.....")
                .setSubText("now")
                .setLargeIcon(largeIconBitmap)
                .setStyle(
                    Notification.BigPictureStyle()
                        .bigPicture(largeIconBitmap)
                        .bigLargeIcon(largeIconBitmap) // Keeps the large icon on the left
                )
                .setSmallIcon(R.drawable.baseline_phone_android_24)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setChannelId(CHANNEL_ID)
                .build()
            nm.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID,
                    "New Channel",
                    NotificationManager.IMPORTANCE_HIGH
                )
            )

        } else {
            notification = Notification.Builder(applicationContext)
                .setContentTitle("Someone")
                .setContentText("Hello Danish,\nIts time to be productive")
                .setSubText("now")
                .setLargeIcon(largeIconBitmap)
                .setStyle(
                    Notification.BigPictureStyle()
                        .bigPicture(largeIconBitmap)
                        .bigLargeIcon(largeIconBitmap) // Keeps the large icon on the left
                )
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.baseline_phone_android_24)
//              .setChannelId(CHANNEL_ID)
                .build()
        }
        nm.notify(notification_ID, notification)
    }
}

