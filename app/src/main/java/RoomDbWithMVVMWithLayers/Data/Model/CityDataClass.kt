package RoomDbWithMVVMWithLayers.Data.Model


data class CityRequest(val country: String, val state: String)

data class CityResponse(
    val error: Boolean,
    val msg: String,
    val data: List<String>
)

