package app.compose_fruits.domain.model

data class Fruit(
    val id: Int?,
    val name: String,
    val genus: String?,
    val family: String?,
    val order: String?,
    val nutritions: Nutritions
)
