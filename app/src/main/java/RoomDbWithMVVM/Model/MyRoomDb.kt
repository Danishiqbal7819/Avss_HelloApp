package RoomDbWithMVVM.Model

//import RoomDbWithMVVMWithLayers.Data.Local.MyRoomDb
//import RoomDbWithMVVMWithLayers.Data.Local.RDbEntity
//import RoomDbWithMVVMWithLayers.Data.Local.UserDao
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


//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//
//@Database(entities = [User::class], version = 1, exportSchema = false)
//abstract class AppDatabase : RoomDatabase() {
//    abstract fun userDao(): UserDao
//val myroomdb=MyroomDb.getIntance(context)
//    companion object {
//        @Volatile
//        private var INSTANCE: AppDatabase? = null
//
//        fun getInstance(context: Context): AppDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    AppDatabase::class.java,
//                    "app_database"
//                ).build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//}
