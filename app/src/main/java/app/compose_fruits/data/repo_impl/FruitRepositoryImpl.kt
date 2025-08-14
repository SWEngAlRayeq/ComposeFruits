package app.compose_fruits.data.repo_impl

import app.compose_fruits.data.model.FruitDto
import app.compose_fruits.data.remote.FruitsApi
import app.compose_fruits.domain.model.Fruit
import app.compose_fruits.domain.model.Nutritions
import app.compose_fruits.domain.repo.FruitRepository
import javax.inject.Inject

class FruitRepositoryImpl @Inject constructor(
    private val api: FruitsApi
) : FruitRepository {
    override suspend fun getAllFruits(): List<Fruit> {
        return api.getAllFruits().map { dto -> dto.toDomain() }
    }

    private fun FruitDto.toDomain(): Fruit {
        return Fruit(
            id = id,
            name = name,
            genus = genus,
            family = family,
            order = order,
            nutritions = Nutritions(
                carbohydrates = nutritions?.carbohydrates,
                protein = nutritions?.protein,
                fat = nutritions?.fat,
                calories = nutritions?.calories,
                sugar = nutritions?.sugar
            )
        )
    }
}