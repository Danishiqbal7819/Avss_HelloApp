package RoomDbWithMVVMWithLayers.UI.ViewModel

import RoomDbWithMVVMWithLayers.Data.API.CountryDataClass
import RoomDbWithMVVMWithLayers.Data.API.StateRequestAndResponse
import RoomDbWithMVVMWithLayers.Data.API.cityRequestAndResponse
import RoomDbWithMVVMWithLayers.Data.Repository.RepositoryAPI
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class APIViewModel(appllication: Application) : AndroidViewModel(appllication) {
    private var repositoryAPI: RepositoryAPI = RepositoryAPI(
        CountryDataClass(),
        StateRequestAndResponse(), cityRequestAndResponse()
    )
    private var _countrylive = MutableLiveData<List<String>>()
    val countrylive: LiveData<List<String>> get() = _countrylive

    private var _statelive = MutableLiveData<List<String>>()
    val statelive: LiveData<List<String>> get() = _statelive

    private var _citylive = MutableLiveData<List<String>>()
    val citylive: LiveData<List<String>> get() = _citylive

    fun getAllCountry() {
        viewModelScope.launch {
            val list = repositoryAPI.getAllCountry()
            _countrylive.postValue(list)
        }
    }

    fun getAllStates(countryname: String) {
        viewModelScope.launch {
            val list = repositoryAPI.getAllStates(countryname)
            _statelive.postValue(list)
        }
    }

    fun getAllCity(countryname: String, Statename: String) {
        viewModelScope.launch {
            val list = repositoryAPI.getAllcity(countryname, Statename)
            _citylive.postValue(list)
        }
    }
}
