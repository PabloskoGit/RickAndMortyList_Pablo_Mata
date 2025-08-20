package com.pmg.rickandmortylist_pablo_mata.di

import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import coil.intercept.Interceptor
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.request.ImageResult
import coil.request.SuccessResult
import coil.util.DebugLogger
import com.pmg.rickandmortylist_pablo_mata.data.remote.api.RickAndMortyApi
import com.pmg.rickandmortylist_pablo_mata.domain.repository.CharacterRepository
import com.pmg.rickandmortylist_pablo_mata.data.repository.CharacterRepositoryImpl
import com.pmg.rickandmortylist_pablo_mata.utils.PARAM_CACHE_KEY
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRickAndMortyApi(): RickAndMortyApi {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val json = Json {
            ignoreUnknownKeys = true
        }

        return Retrofit.Builder()
            .baseUrl(RickAndMortyApi.BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(RickAndMortyApi::class.java)
    }

    @Provides
    @Singleton
    fun providesCharacterRepository(impl: CharacterRepositoryImpl): CharacterRepository {
        return impl
    }

    @Provides
    @Singleton
    fun provideImageLoader(@ApplicationContext context: Context): ImageLoader {
        return ImageLoader.Builder(context)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .memoryCache {
                MemoryCache.Builder(context)
                    .maxSizePercent(0.1) // 10% de la memoria
                    .strongReferencesEnabled(true)
                    .build()
            }
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCache {
                DiskCache.Builder()
                    .maxSizePercent(0.03) // 3% del almacenamiento
                    .directory(context.cacheDir)
                    .build()
            }
            .components {
                add(object : Interceptor {
                    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
                        val request = chain.request

                        val stableCacheKey = request.parameters.value<String>(PARAM_CACHE_KEY)

                        val cachedRequest = if (stableCacheKey != null) {
                            request.newBuilder()
                                .memoryCacheKey(stableCacheKey)
                                .diskCacheKey(stableCacheKey)
                                .networkCachePolicy(CachePolicy.DISABLED)
                                .build()
                        } else {
                            request
                        }

                        val result = chain.proceed(cachedRequest)

                        if (result is SuccessResult) {
                            return result
                        }
                        return chain.proceed(request)
                    }
                })
            }
            .logger(DebugLogger())
            .build()
    }
}