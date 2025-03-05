//package SqlDbWIthMvvm.Model
//
//import SqlDbWIthMvvm.View.SqlActivity2
//import android.Manifest
//import android.content.Context
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.net.Uri
//import android.provider.MediaStore
//import android.widget.ImageView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.appcompat.widget.PopupMenu
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import com.example.helloapp.R
//import java.io.IOException
//import java.io.InputStream
//
//
//class imageclass(var context: Context,var imageId: ImageView):
//    AppCompatActivity() {
//    var activity2:SqlActivity2 = SqlActivity2()
//    private var PICK_IMAGE_REQUEST = 1
//    private val CAMERA_REQUEST_CODE = 100
//    private val CAMERA_PERMISSION_CODE = 101
//
//    fun popmenufunctionForimageSelection1() {
//        val popupMenu = PopupMenu(context,imageId)
//        val inflater = popupMenu.menuInflater
//        inflater.inflate(R.menu.camera_and_gallery_menu, popupMenu.menu)
//        popupMenu.setOnMenuItemClickListener { menuItem ->
//            when (menuItem.itemId) {
//                R.id.camera1 -> {
//                    if (isCameraPermissionGranted()) {
//                        openCamera()
//                    } else {
//                        requestCameraPermission()
//                    }
//                    true
//                }
//
//                R.id.galler1 -> {
//                    val intent=Intent(Intent.ACTION_PICK)
//                    intent.setType("image/*")
//                    startActivityForResult(intent, PICK_IMAGE_REQUEST,)
//                    true
//                }
//
//                else -> false
//            }
//        }
//        popupMenu.show()
//    }
//
//    @Deprecated("Deprecated in Java")
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
//            val imageUri: Uri = data.data!!
//            try {
//
//                val inputStream: InputStream? = contentResolver.openInputStream(imageUri)
//                val bitmap = BitmapFactory.decodeStream(inputStream)
//                val compressedBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, true)
//
//
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }
//        if (requestCode == CAMERA_REQUEST_CODE && resultCode ==RESULT_OK) {
//            val photo: Bitmap = data?.extras?.get("data") as Bitmap
//           val compressedBitmap = Bitmap.createScaledBitmap(photo, 500, 500, true)
////            binding.imageID.setImageBitmap(photo)
//        }
//    }
//    private fun isCameraPermissionGranted(): Boolean {
//        return ContextCompat.checkSelfPermission(
//            context, Manifest.permission.CAMERA
//        ) == PackageManager.PERMISSION_GRANTED
//    }
//
//    private fun requestCameraPermission() {
//        ActivityCompat.requestPermissions(
//            activity2, arrayOf(Manifest.permission.CAMERA),
//            CAMERA_PERMISSION_CODE
//        )
//    }
//
//    private fun openCamera() {
//        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        try {
//            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE,)
//        } catch (e: Exception) {
//           Toast.makeText(this, "$e", Toast.LENGTH_SHORT).show()
//        }
//    }
//}