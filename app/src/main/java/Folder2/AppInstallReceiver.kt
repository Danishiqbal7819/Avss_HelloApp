package Folder2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class AppInstallReceiver : BroadcastReceiver() {
    var myForgroundService = MyForgroundService()
    var data1: String = "App Installed Checker App"


    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_PACKAGE_ADDED) {
            val packageName = intent.data?.schemeSpecificPart
            Toast.makeText(context, "App installed in package:$packageName ", Toast.LENGTH_LONG)
                .show()

            data1 = "App installed successfully in package: $packageName"
            Log.d("AppInstallReceiver", "App installed: $packageName")
            val activity = context as? BroadCastReceiverActvity
            activity?.updateTextView(data1)
            val sharedPreferences =
                context.getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("PackageName", packageName)
            editor.apply()
        } else if (intent.action == Intent.ACTION_PACKAGE_REMOVED) {
            val appname = intent.data?.schemeSpecificPart
            Toast.makeText(context, "Hello App:$appname \n App is Uninstalled:", Toast.LENGTH_LONG)
                .show()
        } else {
            Toast.makeText(context, "No App installed:", Toast.LENGTH_LONG).show()
        }

    }
}