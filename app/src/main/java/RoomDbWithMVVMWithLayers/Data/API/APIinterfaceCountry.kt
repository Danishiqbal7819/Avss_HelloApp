package RoomDbWithMVVMWithLayers.Data.API


import RoomDbWithMVVMWithLayers.Data.Model.CountryResponse
import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


interface CountryApi {
    @GET("v0.1/countries")
    suspend fun getCountries(): CountryResponse
}

object RetrofitInstance {
    private const val BASE_URL = "https://countriesnow.space/api/"

    val api: CountryApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CountryApi::class.java)
    }
}


////////////////////////////////////////////////////////////////////////////////////////////

class CountryDataClass {
    suspend fun CountryDatafun(): MutableList<String> {
        val counterylist: MutableList<String> = mutableListOf()
        val api = RetrofitInstance.api

        return try {
            val response = api.getCountries()
            if (response.data.isNotEmpty()) {
                counterylist.add("None")
                counterylist.addAll(response.data.map { it.country })
                Log.d("Data Country", "Country list: $counterylist")
            } else {
                Log.e("Error", "Response data is empty")
            }
            counterylist
        } catch (e: Exception) {
            Log.e("Error", "Data Fetching Failed: ${e.message}")
            counterylist
        }
    }
}
