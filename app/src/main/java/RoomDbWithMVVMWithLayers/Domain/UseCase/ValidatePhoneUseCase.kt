package RoomDbWithMVVMWithLayers.Domain.UseCase

class ValidatePhoneUseCase {
    operator fun invoke(phone: String): Boolean {
        if (phone.startsWith("+")) {
            return phone.length >= 12 && phone.substring(1).all { it.isDigit() }
        }
        return phone.length == 10 && phone.all { it.isDigit() } && (phone.startsWith("7") || phone.startsWith(
            "8"
        ) || phone.startsWith("9"))
    }
}