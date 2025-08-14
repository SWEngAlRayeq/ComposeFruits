package app.compose_fruits.domain.usecase

import app.compose_fruits.domain.repo.FruitRepository
import javax.inject.Inject

class GetAllFruitsUseCase @Inject constructor(
    private val repository: FruitRepository
) {
    suspend operator fun invoke() = repository.getAllFruits()
}