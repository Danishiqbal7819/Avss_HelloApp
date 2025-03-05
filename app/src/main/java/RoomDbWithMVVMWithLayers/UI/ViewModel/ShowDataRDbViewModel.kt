package RoomDbWithMVVMWithLayers.UI.ViewModel

import RoomDbWithMVVMWithLayers.Data.Local.MyRoomDb
import RoomDbWithMVVMWithLayers.Data.Local.RDbEntity
import RoomDbWithMVVMWithLayers.Data.Local.UserDao
import RoomDbWithMVVMWithLayers.Data.Repository.UserRepositoryRDB
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ShowDataRDbViewModel(application: Application) : AndroidViewModel(application) {
    private var db: UserDao = MyRoomDb.getInstance(application).UserDao()
    private val userRepositoryRDB = UserRepositoryRDB(db)
    private val _usersLiveData = MutableLiveData<List<RDbEntity>>()
    val usersLiveData: LiveData<List<RDbEntity>> get() = _usersLiveData

    fun fetchAllUsers() {
        viewModelScope.launch {
            val users = userRepositoryRDB.getAllUsers()
            _usersLiveData.postValue(users)
        }
    }
}

