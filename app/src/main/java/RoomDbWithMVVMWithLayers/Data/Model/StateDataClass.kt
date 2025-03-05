package RoomDbWithMVVMWithLayers.Data.Model


data class StateRequest(
    val country: String
)

data class StateResponse(
    val error: Boolean,
    val msg: String,
    val data: Data,
)

data class Data(
    val name: String,
    val iso3: String,
    val iso2: String,
    val states: List<State1>,
)

data class State1(
    val name: String,
    val stateCode: String,
)


