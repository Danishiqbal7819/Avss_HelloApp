package RoomDbWithMVVMWithLayers.Data.API

import RoomDbWithMVVMWithLayers.Data.Model.CityRequest
import RoomDbWithMVVMWithLayers.Data.Model.CityResponse
import android.util.Log
import retrofit2.http.Body
import retrofit2.http.POST

interface CityAPI {
    @POST("countries/state/cities")
    suspend fun getAllCity(@Body request: CityRequest): CityResponse
}

//object RetrofitClientCity {
//    private const val BASE_URL = "https://countriesnow.space/api/v0.1/"
//    val instance: Retrofit by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//}

class cityRequestAndResponse {
    suspend fun getALlCity(countryname: String, statename: String): MutableList<String> {
        val citylist = mutableListOf<String>()
        val api = RetrofitClient1.instance.create(CityAPI::class.java)
        val request = CityRequest(countryname, statename)
        try {
            val response = api.getAllCity(request)
            if (response.data.isNotEmpty()) {
                citylist.add("None")
                citylist.addAll(response.data.map { it })
                Log.d("City Data", "City list: $citylist")
                return citylist
            } else {
                Log.e("Error", "Response data is empty")
            }
        } catch (e: Exception) {
            Log.e("Error", "Data Fetching Failed: ${e.message}")
        }
        return citylist
    }
}