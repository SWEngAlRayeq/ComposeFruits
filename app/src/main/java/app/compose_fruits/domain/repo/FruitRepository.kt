package app.compose_fruits.domain.repo

import app.compose_fruits.domain.model.Fruit

interface FruitRepository {
    suspend fun getAllFruits(): List<Fruit>
}