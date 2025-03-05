package SqlDbWIthMvvm.ViewModel

import SqlDbWIthMvvm.Model.Users
import SqlDbWIthMvvm.Repository.UserRespository
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapp.database.DbHelper

class ShowdataViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserRespository
    private val _usersLiveData = MutableLiveData<List<Users>>()
    val usersLiveData: LiveData<List<Users>> get() = _usersLiveData

    init {
        val dbHelper = DbHelper(application)
        repository = UserRespository(dbHelper)
        fetchAllUsers()
    }

    private fun fetchAllUsers() {
        _usersLiveData.value = repository.getAllUsers()
    }
}
