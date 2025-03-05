package Folder2

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.TextView

class MyForgroundService : Service() {

    private lateinit var appInstallReceiver: AppInstallReceiver
    private lateinit var textView1: TextView
    var activity: BroadCastReceiverActvity = BroadCastReceiverActvity()
    var package1: String = ""
    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "service_channel",
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
        val notification: Notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, "service_channel")
                .setContentTitle("Foreground Service")
                .setContentText("Running in the background")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .build()
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        startForeground(1, notification)


    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        appInstallReceiver = AppInstallReceiver()

        return START_STICKY
    }


    override fun onBind(intent: Intent?): IBinder? = null
}
