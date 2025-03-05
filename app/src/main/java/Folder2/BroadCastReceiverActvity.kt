package Folder2

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.helloapp.R


class BroadCastReceiverActvity : AppCompatActivity() {
    private lateinit var appInstallReceiver: AppInstallReceiver
    private lateinit var textView1: TextView
    private lateinit var textView2: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_broad_cast_receiver_actvity)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        textView1 = findViewById<TextView>(R.id.Textview1)
        textView2 = findViewById<TextView>(R.id.Textview2)
        val intent = Intent(this, MyForgroundService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        }


        appInstallReceiver = AppInstallReceiver()

//        val intentFilter1 = IntentFilter(Intent.ACTION_PACKAGE_ADDED).apply {
//            addDataScheme("package")
//        }
//
//
//        registerReceiver(appInstallReceiver, intentFilter1)


        val intentFilter = IntentFilter().apply {
            addAction(Intent.ACTION_PACKAGE_ADDED)
            addAction(Intent.ACTION_PACKAGE_REMOVED)
            addDataScheme("package")
        }

        registerReceiver(appInstallReceiver, intentFilter)


        textView1.text = appInstallReceiver.data1

        getFromSharedPreferences()
    }

    fun updateTextView(data: String) {
        textView2.text = data
        startService(Intent(this, MyForgroundService::class.java))
    }

//        override fun onDestroy() {
//            super.onDestroy()
//            // Unregister the receiver to prevent memory leaks
//            unregisterReceiver(appInstallReceiver)
//        }

    fun getFromSharedPreferences() {
        val sharedPreferences = getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)
        textView1.text = "Last APP installed in package:\n".plus(
            sharedPreferences.getString("PackageName", "").toString()
        )
    }
}

