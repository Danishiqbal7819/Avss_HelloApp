package NavigationDrawerFolder

import ApiService
import CountryRequest
import CountryResponse
import RetrofitClientStates
import RetrofitInstance
import StateResponse
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.api.CityResponse
import com.example.api.CountryApiService
import com.example.api.RetrofitClient
import com.example.api.StateRequest
import com.example.helloapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class NavigationActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var bottomnavigationView: BottomNavigationView
    private lateinit var citieslist: MutableList<String>
    private lateinit var statelist: MutableList<String>
    private lateinit var countrylist: MutableList<String>
    private lateinit var progressBar: ProgressBar
    private lateinit var countryspinners: Spinner
    private lateinit var statespinners: Spinner
    private lateinit var cityspinners: Spinner
    private lateinit var country: String
    private lateinit var state: String
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.my_drawer_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        drawerLayout = findViewById(R.id.my_drawer_layout)
        navigationView = findViewById(R.id.navigationId)
        bottomnavigationView = findViewById(R.id.bottomNavigation)
        progressBar = findViewById(R.id.progressBarOverlay)
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        countryspinners = findViewById(R.id.countryspinners)
        statespinners = findViewById(R.id.statespinners)
        cityspinners = findViewById(R.id.cityspinners)

        citieslist = mutableListOf()
        statelist = mutableListOf()
        countrylist = mutableListOf()
        country = "India"
        state = "Uttar Pradesh"

        countrylist.add("Select country")
        statelist.add("Select State")
        citieslist.add("Select District")

        cityspinners.isEnabled = false
        statespinners.isEnabled = false
        progressBar.visibility = View.GONE
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        bottomnavigationViewFun()
        sidenavigationViewFun()

        CoroutineScope(Dispatchers.Main).launch {
            fetchCountry()
            progressBar.visibility = View.GONE
        }

        spinnerclickFun()

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private fun sidenavigationViewFun() {
        setSupportActionBar(toolbar)
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        drawerLayout.setScrimColor(android.graphics.Color.TRANSPARENT)
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_account -> {
                    Toast.makeText(this, "account", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.nav_settings -> {
                    Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.nav_logout -> {
                    Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.favourite -> {
                    Toast.makeText(this, "Favourite", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.close -> {
                    Toast.makeText(this, "closed", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> false
            }
        }

    }

    private fun bottomnavigationViewFun() {
        bottomnavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menuhome -> {
                    Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.menufav -> {
                    Toast.makeText(this, "Favourites", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.menunotify -> {
                    Toast.makeText(this, "Notification", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> {
                    false
                }
            }
        }
    }

    private fun spinnerclickFun() {
        countryspinners.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position)
                country = selectedItem.toString().trim()
                println("Selected item: $selectedItem")
                CoroutineScope(Dispatchers.Main).launch {
                    fetchstates(country)
                }
                if (position == 0) {
                    progressBar.visibility = View.GONE
                    cityspinners.isEnabled = false
                    statespinners.isEnabled = false
                } else {
                    statespinners.isEnabled = true
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                println("Nothing selected")
            }
        }

        statespinners.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position)
                state = selectedItem.toString().trim()
                println("Selected item: $selectedItem")
                CoroutineScope(Dispatchers.Main).launch {
                    fetchCities(country, state, onSuccess = { cities ->
                        println("Cities in $state, $country: $cities")
                    }, onFailure = { error ->
                        println("Failed to fetch cities: $error")
                    })

                }
                if (position == 0) {
                    progressBar.visibility = View.GONE
                    cityspinners.isEnabled = false
                } else {
                    cityspinners.isEnabled = true
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                println("Nothing selected")
            }
        }
    }

    fun updateSpinner(spinner: Spinner, dataList: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, dataList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        adapter.setNotifyOnChange(true)
        progressBar.visibility = View.GONE
    }

    suspend fun fetchCities(
        country: String,
        state: String,
        onSuccess: (List<String>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val apiService = RetrofitClient.instance.create(CountryApiService::class.java)
        val request = StateRequest(country, state)
        progressBar.visibility = View.VISIBLE


        apiService.getCities(request).enqueue(object : Callback<CityResponse> {
            override fun onResponse(
                call: Call<CityResponse>, response: Response<CityResponse>
            ) {
                if (response.isSuccessful) {
                    progressBar.visibility = View.GONE
                    val cityResponse = response.body()
                    if (cityResponse != null) {
                        citieslist.clear()
                        citieslist.add("Select City")
                        citieslist.addAll(cityResponse.data)

                        updateSpinner(cityspinners, citieslist)
                        onSuccess(cityResponse.data)

                    } else {
                        onFailure("Response was null")
                        progressBar.visibility = View.GONE
                    }
                } else {
                    progressBar.visibility = View.GONE
                    onFailure("Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<CityResponse>, t: Throwable) {
                onFailure("API call failed: ${t.message}")
                progressBar.visibility = View.GONE
            }
        })

    }

    suspend fun fetchstates(countrys: String) {
        progressBar.visibility = View.VISIBLE
        val apiService = RetrofitClientStates.instance.create(ApiService::class.java)
        val request = CountryRequest(country = countrys)
        apiService.getStates(request).enqueue(object : Callback<StateResponse> {
            override fun onResponse(call: Call<StateResponse>, response: Response<StateResponse>) {
                if (response.isSuccessful) {
                    progressBar.visibility = View.GONE
                    val stateNames = response.body()?.data?.states?.map { it.name } ?: emptyList()
                    statelist.clear()
                    statelist.add("Select State")
                    statelist.addAll(stateNames)
                    updateSpinner(statespinners, statelist)
                    println("States in India: $stateNames")
                } else {
                    progressBar.visibility = View.GONE
                    println("Request failed with status: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<StateResponse>, t: Throwable) {
                println("Request failed: ${t.message}")
                progressBar.visibility = View.GONE
            }
        })
    }

    suspend fun fetchCountry() {
        val api = RetrofitInstance.api
        api.getCountries().enqueue(object : Callback<CountryResponse> {
            override fun onResponse(
                call: Call<CountryResponse>, response: Response<CountryResponse>
            ) {
                if (response.isSuccessful) {
                    val countryResponse = response.body()
                    if (countryResponse != null) {
                        val countryList = countryResponse.data.map { it.country }
                        countrylist.clear()
                        countrylist.add("Select Country")
                        countrylist.addAll(countryList)
                        progressBar.visibility = View.GONE
                        updateSpinner(countryspinners, countrylist)
                        println("Country List: $countryList")
                    } else {
                        println("Response body is null")
                    }
                } else {
                    println("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<CountryResponse>, t: Throwable) {
                println("Request failed: ${t.message}")
                progressBar.visibility = View.GONE
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.bottommenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menufav -> {
                Toast.makeText(this, "fav clicked", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.menunotify -> {
                Toast.makeText(this, "notify clicked", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) true
        else {
            super.onOptionsItemSelected(item)
        }
    }

}



