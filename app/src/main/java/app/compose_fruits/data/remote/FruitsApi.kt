package app.compose_fruits.data.remote

import app.compose_fruits.data.model.FruitDto
import retrofit2.http.GET

interface FruitsApi {

    @GET("api/fruit/all")
    suspend fun getAllFruits(): List<FruitDto>

}