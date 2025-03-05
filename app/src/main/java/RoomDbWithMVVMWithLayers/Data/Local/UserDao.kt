package RoomDbWithMVVMWithLayers.Data.Local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {

    @Query("Select * From users")
    suspend fun getAllUser(): List<RDbEntity>

    @Insert
    suspend fun insertAllUsers(rDbEntity: RDbEntity): Long

    @Update
    suspend fun UpdateUser(rDbEntity: RDbEntity): Int

    @Delete
    fun DeleteUsers(rDbEntity: RDbEntity): Int

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Int): RDbEntity
}

