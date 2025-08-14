package app.compose_fruits.di

import app.compose_fruits.data.remote.FruitsApi
import app.compose_fruits.data.repo_impl.FruitRepositoryImpl
import app.compose_fruits.domain.repo.FruitRepository
import app.compose_fruits.domain.usecase.GetAllFruitsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val BASE_URL = "https://www.fruityvice.com/"

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(logging: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

    @Provides
    @Singleton
    fun provideFruitsApi(client: OkHttpClient): FruitsApi =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FruitsApi::class.java)

    @Provides
    @Singleton
    fun provideFruitRepository(api: FruitsApi): FruitRepository = FruitRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideGetAllFruitsUseCase(repo: FruitRepository): GetAllFruitsUseCase = GetAllFruitsUseCase(repo)
}