package SqlDbWIthMvvm.ViewModel

import SqlDbWIthMvvm.Model.Users
import SqlDbWIthMvvm.Repository.UserRespository
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapp.database.DbHelper

class SqlActivity2ViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserRespository
    private val _userLiveData = MutableLiveData<Users>()
    val userLiveData: LiveData<Users> get() = _userLiveData

    init {
        val dbHelper = DbHelper(application)
        repository = UserRespository(dbHelper)
    }

    fun AddAllUsers(
        firstName: String,
        middleName: String,
        lastName: String,
        phone: String,
        email: String,
        image: ByteArray
    ): Long {
        return repository.addData(firstName, middleName, lastName, phone, email, image)
    }

    fun updateusers(
        userId: Int,
        firstName: String,
        middleName: String,
        lastName: String,
        phone: String,
        email: String,
        image: ByteArray
    ): Boolean {
        return repository.updateusers(userId, firstName, middleName, lastName, phone, email, image)
    }

    fun getUserById(userId: Int) {
        val user = repository.getUserById(userId)
        _userLiveData.postValue(user)
    }

}

