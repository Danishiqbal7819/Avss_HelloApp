package RoomDbWithMVVMWithLayers.Data.API

import RoomDbWithMVVMWithLayers.Data.Model.StateRequest
import RoomDbWithMVVMWithLayers.Data.Model.StateResponse
import android.util.Log
import retrofit2.http.Body
import retrofit2.http.POST

interface StateApi {
    @POST("countries/states")
    suspend fun fetchStates(@Body request: StateRequest): StateResponse
}


class StateRequestAndResponse {
    suspend fun getALlStates(countryname: String): MutableList<String> {
        val statelist = mutableListOf<String>()
        val api = RetrofitClient1.instance.create(StateApi::class.java)
        val request = StateRequest(country = countryname)
        try {
            val response = api.fetchStates(request)
            if (response.data.states.isNotEmpty()) {
                statelist.add("None")
                statelist.addAll(response.data.states.map { it.name })
                Log.d("Data State", "State list: $statelist")
                return statelist
            } else {
                Log.e("Error", "Response data is empty")
            }
        } catch (e: Exception) {
            Log.e("Error", "Data Fetching Failed: ${e.message}")
        }
        return statelist
    }
}

