package RoomDbWithMVVMWithLayers.Data.Model

data class CountryResponse(
    val msg: String,
    val data: List<Country>
)

data class Country(
    val iso2: String,
    val iso3: String,
    val country: String,
    val cities: List<String>
)
