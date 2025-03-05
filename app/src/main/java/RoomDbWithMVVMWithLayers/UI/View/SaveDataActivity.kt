package RoomDbWithMVVMWithLayers.UI.View

import RoomDbWithMVVMWithLayers.Data.Local.RDbEntity
import RoomDbWithMVVMWithLayers.UI.ViewModel.APIViewModel
import RoomDbWithMVVMWithLayers.UI.ViewModel.SaveDataViewModel
import RoomDbWithMVVMWithLayers.UI.ViewModel.UserViewModel
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
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

    private lateinit var apiviewmodel: APIViewModel
    private lateinit var countrySpinner: Spinner
    private lateinit var stateSpinner: Spinner
    private lateinit var citySpinner: Spinner
    private lateinit var countrylist: MutableList<String>
    private lateinit var statelist: MutableList<String>
    private lateinit var citylist: MutableList<String>
    private lateinit var userviewmodel: UserViewModel
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
    private lateinit var country_name: String
    private lateinit var state_name: String
    private lateinit var city_name: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaveDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        saveDataViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(SaveDataViewModel::class.java)


        apiviewmodel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(APIViewModel::class.java)


        userviewmodel = ViewModelProvider(this).get(UserViewModel::class.java)


        userviewmodel.toastMessage.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                userviewmodel.clearToast()
            }
        }

        binding.statespinners.isEnabled = false
        binding.cityspinners.isEnabled = false

        MySpinnerFunction()
        spinnerclickFunctionality()

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
                val address = it.address_name.split(" \\ ")
                binding.countryTextView.text = address[0].toString()
                binding.stateTextView.text = address[1].toString()
                binding.cityTextView.text = address[2].toString()

                val bitmap = byteArrayToBitmap(it.image)
                binding.imageID.setImageBitmap(bitmap)
            }
        }


        binding.btnSave.setOnClickListener {
            first_nameText = binding.etFullName.text.toString().trim()
            middle_nameText = binding.etMiddleName.text.toString().trim()
            last_nameText = binding.etLastName.text.toString().trim()
            phone_nameText = binding.etphone.text.toString().trim()
            email_nameText = binding.etEmail.text.toString().trim()
            val country1 = binding.countryTextView.text.toString()
            val state1 = binding.stateTextView.text.toString()
            val city1 = binding.cityTextView.text.toString()
            if (userviewmodel.validateUser(
                    email_nameText,
                    phone_nameText,
                    first_nameText,
                    country1,
                    state1,
                    city1
                )
            ) {

                if (updatevalue == 1) {
                    rdbEntity = RDbEntity(
                        userid,
                        first_nameText,
                        middle_nameText,
                        last_nameText,
                        phone_nameText,
                        email_nameText,
                        "${country1} \\ ${state1} \\ ${city1}",
                        imageViewToByteArray(binding.imageID)
                    )
                    if (saveDataViewModel.updateuser(rdbEntity) > 0) {
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
                        "${country1} \\ ${state1} \\ ${city1}",
                        imageViewToByteArray(binding.imageID)
                    )
                    if (saveDataViewModel.insertUsers(rdbEntity) > 0) {
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

    /////////////////////////////////////////////////////////////////
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

    ////////////////////////////////////////////////////////////////////////////////
    private fun MySpinnerFunction() {
        countrylist = mutableListOf()
        statelist = mutableListOf()
        citylist = mutableListOf()
        country_name = "None"
        state_name = "None"
        city_name = "None"

        val adapter1 =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, countrylist)
        val adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, statelist)
        val adapter3 = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, citylist)
        apiviewmodel.getAllCountry()

        apiviewmodel.countrylive.observe(this) {
            countrylist.clear()
            countrylist.addAll(it)
            adapter1.notifyDataSetChanged()
        }
        apiviewmodel.statelive.observe(this) {
            statelist.clear()
            statelist.addAll(it)
            adapter2.notifyDataSetChanged()
        }
        apiviewmodel.citylive.observe(this) {
            citylist.clear()
            citylist.addAll(it)
            adapter3.notifyDataSetChanged()
        }
        countrySpinner = binding.countryspinners
        stateSpinner = binding.statespinners
        citySpinner = binding.cityspinners
        countrySpinner.adapter = adapter1
        stateSpinner.adapter = adapter2
        citySpinner.adapter = adapter3
    }

    private fun spinnerclickFunctionality() {
        binding.countryspinners.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    val selectedItem = parent?.getItemAtPosition(position)

                    println("Selected item: $selectedItem")
                    if (position == 0) {
                        binding.progressBarOverlay.visibility = View.GONE
                        binding.cityspinners.isEnabled = false
                        binding.statespinners.isEnabled = false
                    } else {
                        binding.countryTextView.text = selectedItem.toString().trim()
                        country_name = binding.countryTextView.text.toString()
                        apiviewmodel.getAllStates(country_name)
                        binding.statespinners.isEnabled = true
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    println("Nothing selected")
                }
            }

        binding.statespinners.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position)
                println("Selected item: $selectedItem")
                if (position == 0) {
                    binding.progressBarOverlay.visibility = View.GONE
                    binding.cityspinners.isEnabled = false
                } else {
                    binding.stateTextView.text = selectedItem.toString().trim()
                    state_name = binding.stateTextView.text.toString()
                    apiviewmodel.getAllCity(country_name, state_name)
                    binding.cityspinners.isEnabled = true
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                println("Nothing selected")
            }
        }
        binding.cityspinners.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position)
                binding.cityTextView.text = selectedItem.toString().trim()
                city_name = binding.cityTextView.text.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                println("Nothing selected")
            }
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////


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

    private fun byteArrayToBitmap(imageBytes: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}



