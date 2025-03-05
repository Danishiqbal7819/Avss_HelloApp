package SqliteDatabase

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.helloapp.R
import com.example.helloapp.databinding.ActivitySqlBinding
import com.example.myapp.database.DatabaseHelper
import com.example.myapp.database.User
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

class SqlActivity : AppCompatActivity() {

    private var bitmapicon: Bitmap? = null
    private var userid: Int = 0
    private var updatevalue = 0
    private lateinit var emailedittext: EditText
    private lateinit var phoneedittext: EditText
    private lateinit var lastnameedittext: EditText
    private lateinit var middlenameedittext: EditText
    private lateinit var firstnameedittext: EditText
    private lateinit var firstname: String
    private lateinit var middlename: String
    private lateinit var lastname: String
    private lateinit var phonetext: String
    private lateinit var emailname: String

    private lateinit var imageByteArray: ByteArray
    private lateinit var binding: ActivitySqlBinding
    private var PICK_IMAGE_REQUEST = 1
    private val CAMERA_REQUEST_CODE = 100
    private val CAMERA_PERMISSION_CODE = 101
    private lateinit var userDatalist: MutableList<User>

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(R.layout.activity_sql)
        binding = ActivitySqlBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        userDatalist = mutableListOf()

        firstnameedittext = findViewById<EditText>(R.id.etFullName)
        middlenameedittext = findViewById<EditText>(R.id.etMiddleName)
        lastnameedittext = findViewById<EditText>(R.id.etLastName)
        phoneedittext = findViewById<EditText>(R.id.etphone)
        emailedittext = findViewById<EditText>(R.id.etEmail)


        val intent = intent
        firstnameedittext.setText(intent.getStringExtra("firstname") ?: "")
        middlenameedittext.setText(intent.getStringExtra("middlename") ?: "")
        lastnameedittext.setText(intent.getStringExtra("lastname") ?: "")
        phoneedittext.setText(intent.getStringExtra("phone") ?: "")
        emailedittext.setText(intent.getStringExtra("email") ?: "")
        userid = intent.getIntExtra("userId", 0)
        updatevalue = intent.getIntExtra("updatevalue", 0)
        binding.btnSave.text = intent.getStringExtra("btntext") ?: "Save"

        bitmapicon =
            BitmapFactory.decodeResource(applicationContext.resources, R.drawable.useraccount)

        if (updatevalue == 1) {
            try {
                val updatedimage = intent.getParcelableExtra<Bitmap>("image") ?: bitmapicon
                binding.imageID.setImageBitmap(updatedimage)
            } catch (e: Exception) {
                binding.imageID.setImageBitmap(bitmapicon)
                Toast.makeText(this, "Sql::$e", Toast.LENGTH_SHORT).show()
            }
        } else {
            binding.imageID.setImageBitmap(bitmapicon)
        }
        val dbHelper = DatabaseHelper(this)


        binding.imageID.setOnClickListener {
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

                    else -> false
                }
            }
            popupMenu.show()
        }

        binding.btnShow.setOnClickListener {
            val intent = Intent(this, ShowDataActvity::class.java)
            startActivity(intent)
        }


        binding.btnSave.setOnClickListener {

            firstname = firstnameedittext.text.toString().trim()
            middlename = middlenameedittext.text.toString().trim()
            lastname = lastnameedittext.text.toString().trim()
            phonetext = phoneedittext.text.toString().trim()
            emailname = emailedittext.text.toString().trim()


            if (imageValidation() && isValidEmail(emailname) && firstnameedittext.text.isNotBlank() && phonetext.isNotBlank() && emailname.isNotBlank() && (phonetext.startsWith(
                    "7"
                ) || phonetext.startsWith("8") || phonetext.startsWith("9") || phonetext.startsWith(
                    "+"
                ))
            ) {
                imageByteArray = imageViewToByteArray(binding.imageID)
                if (updatevalue == 1) {
                    val intent = Intent(applicationContext, ShowDataActvity::class.java)
                    val result = dbHelper.updateUser(
                        userid,
                        firstname,
                        middlename,
                        lastname,
                        phonetext,
                        emailname,
                        imageByteArray
                    )
                    if (result) {
                        clearEditext()
                        startActivity(intent)
                        finish()
                        Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val result =
                        dbHelper.insertUser(
                            firstname,
                            middlename,
                            lastname,
                            phonetext,
                            emailname,
                            imageByteArray
                        )

                    if (result > 0) {
                        clearEditext()
                        Toast.makeText(this, "User Inserted Successfully", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(this, "Insertion Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            } else if (!isValidEmail(emailname)) {
                Toast.makeText(this, "invalid email or empty field", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    this,
                    "plz fill all the details and phone must start with 7,8,9",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun clearEditext() {
        binding.imageID.setImageBitmap(bitmapicon)
        firstnameedittext.text.clear()
        middlenameedittext.text.clear()
        lastnameedittext.text.clear()
        phoneedittext.text.clear()
        emailedittext.text.clear()
    }


    fun imageViewToByteArray(imageView: ImageView): ByteArray {
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream) // Compress to reduce size
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

                val imageByteArray = bitmapToByteArray(compressedBitmap)

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


    fun imageValidation(): Boolean {
        val drawable = binding.imageID.drawable
        if (drawable != null && drawable is BitmapDrawable) {
            val bitmap =
                drawable.bitmap//            Toast.makeText(this, "ImageView has no bitmap", Toast.LENGTH_SHORT).show()
            //            Toast.makeText(this, "ImageView has a bitmap", Toast.LENGTH_SHORT).show()
            return bitmap != null
        } else {
//        Toast.makeText(this, "ImageView is empty", Toast.LENGTH_SHORT).show()
            return false
        }
    }

    private fun byteArrayToBitmap(imageBytes: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
}