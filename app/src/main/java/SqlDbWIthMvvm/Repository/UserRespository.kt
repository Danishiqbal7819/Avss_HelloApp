package SqlDbWIthMvvm.Repository

import SqlDbWIthMvvm.Model.Users
import com.example.myapp.database.DbHelper

class UserRespository(private val dbHelper: DbHelper) {
    fun getAllUsers(): List<Users> {
        return dbHelper.getAllUsers()
    }

    fun addData(
        firstName: String,
        middleName: String,
        lastName: String,
        phone: String,
        email: String,
        image: ByteArray
    ): Long {
        return dbHelper.insertUser(firstName, middleName, lastName, phone, email, image)
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
        return dbHelper.updateUser(userId, firstName, middleName, lastName, phone, email, image)
    }

    fun getUserById(userId: Int): Users {
        return dbHelper.getUserById(userId)!!
    }

}
