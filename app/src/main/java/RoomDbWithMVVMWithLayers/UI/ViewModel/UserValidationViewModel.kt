package RoomDbWithMVVMWithLayers.UI.ViewModel

import RoomDbWithMVVMWithLayers.Domain.UseCase.ValidateAddressUseCase
import RoomDbWithMVVMWithLayers.Domain.UseCase.ValidateEmailUseCase
import RoomDbWithMVVMWithLayers.Domain.UseCase.ValidateNameUseCase
import RoomDbWithMVVMWithLayers.Domain.UseCase.ValidatePhoneUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {
    private val validateEmailUseCase: ValidateEmailUseCase
    private val validatePhoneUseCase: ValidatePhoneUseCase
    private val validateNameUseCase: ValidateNameUseCase
    private val validateAddressUseCase: ValidateAddressUseCase
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    init {
        validateEmailUseCase = ValidateEmailUseCase()
        validatePhoneUseCase = ValidatePhoneUseCase()
        validateNameUseCase = ValidateNameUseCase()
        validateAddressUseCase = ValidateAddressUseCase()
    }

    fun validateUser(
        email: String,
        phone: String,
        name: String,
        country: String,
        state: String,
        city: String
    ): Boolean {
        return when {
            !validateNameUseCase(name) -> {
                _toastMessage.value = "Empty Name Field"
                false
            }

            !validatePhoneUseCase(phone) -> {
                _toastMessage.value = "Invalid Phone Number"
                false
            }

            !validateEmailUseCase(email) -> {
                _toastMessage.value = "Invalid Email Address"
                false
            }

            !validateAddressUseCase(country, state, city) -> {
                _toastMessage.value = "Select All Fields Of Address"
                false
            }

            else -> {
                true
            }
        }
    }

    fun clearToast() {
        _toastMessage.value = null
    }
}
