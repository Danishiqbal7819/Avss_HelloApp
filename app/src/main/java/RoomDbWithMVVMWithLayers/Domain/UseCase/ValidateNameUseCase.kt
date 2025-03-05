package RoomDbWithMVVMWithLayers.Domain.UseCase

class ValidateNameUseCase {
    operator fun invoke(name: String): Boolean {
        return name.isNotEmpty()
    }
}

