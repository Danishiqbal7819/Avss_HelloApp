package RoomDbWithMVVMWithLayers.Data.Repository

import RoomDbWithMVVMWithLayers.Data.Local.RDbEntity
import RoomDbWithMVVMWithLayers.Data.Local.UserDao
import kotlinx.coroutines.runBlocking


class UserRepositoryRDB(private val db: UserDao) {
    fun insertUsers(rDbEntity: RDbEntity): Long {
        return runBlocking {
            db.insertAllUsers(rDbEntity)
        }
    }

    suspend fun getAllUsers(): List<RDbEntity> {
        return db.getAllUser()
    }

    fun updateuser(rDbEntity: RDbEntity): Int {
        return runBlocking {
            db.UpdateUser(rDbEntity)
        }
    }

    fun getUserById(id: Int): RDbEntity {
        return runBlocking {
            db.getUserById(id)
        }
    }
}

