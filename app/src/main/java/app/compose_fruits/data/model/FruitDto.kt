package app.compose_fruits.data.model

import com.google.gson.annotations.SerializedName

data class FruitDto(
    val genus: String?,
    val name: String,
    val id: Int?,
    val family: String?,
    val order: String?,
    @SerializedName("nutritions") val nutritions: NutritionsDto?
)
