import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

data class CountryRequest(val country: String)

data class State(
    val name: String,
    val state_code: String
)

data class CountryData(
    val states: List<State>
)

data class StateResponse(
    val data: CountryData
)

interface ApiService {
    @POST("countries/states")
    fun getStates(@Body request: CountryRequest): Call<StateResponse>
}

object RetrofitClientStates {
    private const val BASE_URL = "https://countriesnow.space/api/v0.1/"


    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}