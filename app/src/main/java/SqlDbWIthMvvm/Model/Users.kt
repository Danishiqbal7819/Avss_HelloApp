package SqlDbWIthMvvm.Model

data class Users(
    val id: Int,
    val firstName: String,
    val middleName: String,
    val lastName: String,
    val phone: String,
    val email: String,
    val image: ByteArray
)
