package RoomDbWithMVVMWithLayers.Domain.UseCase

class ValidateAddressUseCase {
    operator fun invoke(Country: String, State: String, City: String): Boolean {
        return !(Country == "None" || Country.isEmpty() || State == "None" || State.isEmpty() || State.isEmpty())
    }
}
