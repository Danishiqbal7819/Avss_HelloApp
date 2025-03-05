package RoomDbWithMVVMWithLayers.Data.Repository

import RoomDbWithMVVMWithLayers.Data.API.CountryDataClass
import RoomDbWithMVVMWithLayers.Data.API.StateRequestAndResponse
import RoomDbWithMVVMWithLayers.Data.API.cityRequestAndResponse
import android.util.Log


class RepositoryAPI(
    private val countryData: CountryDataClass,
    private val stateRequestAndResponse: StateRequestAndResponse,
    private val cityRequestAndResponse: cityRequestAndResponse
) {
    suspend fun getAllCountry(): List<String> {
//             Log.d("Data in Repo","${countryData.CountryDatafun()}")
        return countryData.CountryDatafun()
    }

    suspend fun getAllStates(countryname: String): List<String> {
//             Log.d("Data in Repo","${stateRequestAndResponse.getALlStates(countryname)}")
        return stateRequestAndResponse.getALlStates(countryname)
    }

    suspend fun getAllcity(countryname: String, statename: String): List<String> {
        Log.d("Data(City) in Repo", "${cityRequestAndResponse.getALlCity(countryname, statename)}")
        return cityRequestAndResponse.getALlCity(countryname, statename)
    }
}


