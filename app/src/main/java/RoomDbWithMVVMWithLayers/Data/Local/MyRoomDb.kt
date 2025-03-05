package RoomDbWithMVVMWithLayers.Data.Local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RDbEntity::class], version = 1, exportSchema = false)
abstract class MyRoomDb : RoomDatabase() {

    abstract fun UserDao(): UserDao

    companion object {
        private var INSTANCE: MyRoomDb? = null

        fun getInstance(context: Context): MyRoomDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyRoomDb::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }

    }
}
