package RoomDbWithMVVMWithLayers.Domain.UseCase


class ValidateEmailUseCase {
    operator fun invoke(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
