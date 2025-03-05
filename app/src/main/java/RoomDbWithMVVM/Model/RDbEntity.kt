package RoomDbWithMVVM.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class RDbEntity(

    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val first_name: String,
    @ColumnInfo val middle_name: String,
    @ColumnInfo val last_name: String,
    @ColumnInfo val phone_name: String,
    @ColumnInfo val email_name: String,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB) val image: ByteArray
)

//@Entity(tableName = "users")
//data class User(
//    @PrimaryKey(autoGenerate = true) val id: Int = 0,
//    val name: String,
//    val age: Int
//)
