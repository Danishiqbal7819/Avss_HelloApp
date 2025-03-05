import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class CountryResponse(
    val msg: String,
    val data: List<Country>
)

data class Country(
    val iso2: String,
    val iso3: String,
    val country: String,
    val cities: List<String>
)

interface CountryApi {
    @GET("v0.1/countries")
    fun getCountries(): Call<CountryResponse>
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


//   suspend fun fetchCountry():MutableList<String> {
//    val countrylist:MutableList<String>
//    countrylist= mutableListOf()
//    val api = RetrofitInstance.api
//    api.getCountries().enqueue(object : Callback<CountryResponse> {
//        override fun onResponse(
//            call: Call<CountryResponse>, response: Response<CountryResponse>
//        ) {
//            if (response.isSuccessful) {
//                val countryResponse = response.body()
//                if (countryResponse != null) {
//                    val countryList = countryResponse.data.map { it.country }
//                    countrylist.clear()
//                    countrylist.add("Select Country")
//                    countrylist.addAll(countryList)
//                    println("Country List: $countryList")
//                } else {
//                    println("Response body is null")
//                }
//            } else {
//                println("Error: ${response.code()}")
//            }
//        }
//        override fun onFailure(call: Call<CountryResponse>, t: Throwable) {
//            println("Request failed: ${t.message}")
//        }
//    })
//    return  countrylist
//
//}
