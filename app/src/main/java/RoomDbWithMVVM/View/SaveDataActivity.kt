package RoomDbWithMVVM.View

//import RoomDbWithMVVMWithLayers.Data.Local.RDbEntity
//import RoomDbWithMVVM.ViewModel.SaveDataViewModel
import RoomDbWithMVVM.Model.RDbEntity
import RoomDbWithMVVM.ViewModel.SaveDataViewModel
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.helloapp.R
import com.example.helloapp.databinding.ActivitySaveDataBinding
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

class SaveDataActivity : AppCompatActivity() {
    private var bitmapicon: Bitmap? = null
    private var userid: Int = 0
    private lateinit var middle_nameText: String
    private var updatevalue = 0
    private lateinit var first_nameText: String
    private lateinit var last_nameText: String
    private lateinit var phone_nameText: String
    private lateinit var email_nameText: String
    private lateinit var rdbEntity: RDbEntity
    private lateinit var saveDataViewModel: SaveDataViewModel
    private lateinit var binding: ActivitySaveDataBinding
    private var PICK_IMAGE_REQUEST = 1
    private val CAMERA_REQUEST_CODE = 100
    private val CAMERA_PERMISSION_CODE = 101

    //    private lateinit var datalist: MutableList<RDbEntity>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaveDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        saveDataViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(SaveDataViewModel::class.java)

        val intent = intent
        userid = intent.getIntExtra("userId", -1)
        updatevalue = intent.getIntExtra("updatevalue", 0)
        binding.btnSave.text = intent.getStringExtra("btntext") ?: "Save"
        bitmapicon =
            BitmapFactory.decodeResource(applicationContext.resources, R.drawable.useraccount)
        binding.imageID.setImageBitmap(bitmapicon)

        if (userid != -1) {
            saveDataViewModel.getUserById(userid)
        }

        saveDataViewModel.userLiveData.observe(this) { user ->
            user?.let {
                binding.etFullName.setText(it.first_name)
                binding.etMiddleName.setText(it.middle_name)
                binding.etLastName.setText(it.last_name)
                binding.etphone.setText(it.phone_name)
                binding.etEmail.setText(it.email_name)
                val bitmap = byteArrayToBitmap(it.image)
                binding.imageID.setImageBitmap(bitmap)
            }
        }

        binding.btnSave.setOnClickListener {
            if (getTextFunctionValidation()) {
                if (updatevalue == 1) {
                    rdbEntity = RDbEntity(
                        userid,
                        first_nameText,
                        middle_nameText,
                        last_nameText,
                        phone_nameText,
                        email_nameText,
                        imageViewToByteArray(binding.imageID)
                    )
                    val result = saveDataViewModel.updateuser(rdbEntity)
                    if (result > 0) {
                        Toast.makeText(this, "Update Successful", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    rdbEntity = RDbEntity(
                        0,
                        first_nameText,
                        middle_nameText,
                        last_nameText,
                        phone_nameText,
                        email_nameText,
                        imageViewToByteArray(binding.imageID)
                    )
                    val result = saveDataViewModel.insertUsers(rdbEntity)
                    if (result > 0) {
                        Toast.makeText(this, "Insertion Successful", Toast.LENGTH_SHORT).show()
                        clearEditext()
                    } else {
                        Toast.makeText(this, "Insertion Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            } else return@setOnClickListener

        }

        binding.btnShow.setOnClickListener {
            val intent = Intent(this, ShowRDbDataActivity::class.java)
            startActivity(intent)
        }

        binding.imageID.setOnClickListener {
            popmenufunctionForimageSelection()
        }
    }


    private fun getTextFunctionValidation(): Boolean {
        first_nameText = binding.etFullName.text.toString().trim()
        middle_nameText = binding.etMiddleName.text.toString().trim()
        last_nameText = binding.etLastName.text.toString().trim()
        phone_nameText = binding.etphone.text.toString().trim()
        email_nameText = binding.etEmail.text.toString().trim()

        if (first_nameText.isEmpty()) {
            Toast.makeText(this, "Name field cannot be empty", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!isValidEmail(email_nameText)) {
            Toast.makeText(this, "Invalid or empty email", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!(phone_nameText.startsWith("7") || phone_nameText.startsWith("8") ||
                    phone_nameText.startsWith("9") || phone_nameText.startsWith("+"))
        ) {
            Toast.makeText(this, "Phone number must start with 7, 8, 9, or +", Toast.LENGTH_SHORT)
                .show()
            return false
        }
        return true
    }

    private fun popmenufunctionForimageSelection() {
        val popupMenu = PopupMenu(this, binding.imageID)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.camera_and_gallery_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.camera1 -> {
                    if (isCameraPermissionGranted()) {
                        openCamera()
                    } else {
                        requestCameraPermission()
                    }
                    true
                }

                R.id.galler1 -> {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.setType("image/*")
                    startActivityForResult(intent, PICK_IMAGE_REQUEST)
                    true
                }

                R.id.Remove_Dp -> {
                    binding.imageID.setImageBitmap(bitmapicon)
                    Toast.makeText(this@SaveDataActivity, "Picture Removed", Toast.LENGTH_SHORT)
                        .show()
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }

    fun clearEditext() {
        binding.imageID.setImageBitmap(bitmapicon)
        binding.etFullName.text.clear()
        binding.etMiddleName.text.clear()
        binding.etLastName.text.clear()
        binding.etphone.text.clear()
        binding.etEmail.text.clear()
        binding.etFullName.text.clear()
    }

    fun imageViewToByteArray(imageView: ImageView): ByteArray {
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            val imageUri: Uri = data.data!!
            try {
                val inputStream: InputStream? = contentResolver.openInputStream(imageUri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                val compressedBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, true)

                binding.imageID.setImageBitmap(compressedBitmap)

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            val photo: Bitmap = data?.extras?.get("data") as Bitmap
            binding.imageID.setImageBitmap(photo)
        }
    }

    private fun isCameraPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_CODE
        )
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
        } catch (e: Exception) {
            Toast.makeText(this, "$e", Toast.LENGTH_SHORT).show()
        }
    }

    fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}"
        return email.matches(emailPattern.toRegex())
    }

    private fun byteArrayToBitmap(imageBytes: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}


