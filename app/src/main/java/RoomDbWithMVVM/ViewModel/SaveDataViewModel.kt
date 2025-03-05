package RoomDbWithMVVM.ViewModel

//import RoomDbWithMVVMWithLayers.Data.Local.MyRoomDb
//import RoomDbWithMVVMWithLayers.Data.Local.RDbEntity
//import RoomDbWithMVVMWithLayers.Data.Local.UserDao
import RoomDbWithMVVM.Model.MyRoomDb
import RoomDbWithMVVM.Model.RDbEntity
import RoomDbWithMVVM.Model.UserDao
import RoomDbWithMVVM.Repository.UserRepositoryRDB
//import RoomDbWithMVVMWithLayers.Data.Local.MyRoomDb
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SaveDataViewModel(application: Application) : AndroidViewModel(application) {
    private var db: UserDao
    private var userRepositoryRDB: UserRepositoryRDB
    private val _userLiveData = MutableLiveData<RDbEntity>()
    val userLiveData: LiveData<RDbEntity> get() = _userLiveData

    init {
        db = MyRoomDb.getInstance(application).UserDao()
        userRepositoryRDB = UserRepositoryRDB(db)
    }

    fun insertUsers(rDbEntity: RDbEntity): Long {
        val result = userRepositoryRDB.insertUsers(rDbEntity)
        return result
    }

    fun updateuser(rDbEntity: RDbEntity): Int {

        return userRepositoryRDB.updateuser(rDbEntity)

    }

    fun getUserById(userId: Int) {
        val user = userRepositoryRDB.getUserById(userId)
        _userLiveData.postValue(user)
    }


}