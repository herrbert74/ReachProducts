package com.babestudios.reachproducts.di

import com.babestudios.reachproducts.BuildConfig
import com.babestudios.reachproducts.data.local.DatabaseContract
import com.babestudios.reachproducts.data.network.ReachProductsRepository
import com.babestudios.reachproducts.data.network.ReachProductsRepositoryContract
import com.babestudios.reachproducts.data.network.ReachProductsService
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
@Suppress("unused")
@InstallIn(SingletonComponent::class)
class NetworkModule {
	@Provides
	@Singleton
	internal fun provideReachProductsRetrofit(): Retrofit {
		val logging = HttpLoggingInterceptor()
		logging.level = HttpLoggingInterceptor.Level.BODY

		val httpClient = OkHttpClient.Builder()

		httpClient.addInterceptor(logging)
		return Retrofit.Builder()//
			.baseUrl(BuildConfig.REACH_PRODUCTS_BASE_URL)//
			.addConverterFactory(GsonConverterFactory.create())//
			.client(httpClient.build())//
			.build()
	}

	@Provides
	@Singleton
	internal fun provideReachProductsService(retroFit: Retrofit): ReachProductsService {
		return retroFit.create(ReachProductsService::class.java)
	}

	@Provides
	@Singleton
	fun provideReachProductsRepository(
		reachProductsService: ReachProductsService,
		database: DatabaseContract
	): ReachProductsRepositoryContract {
		return ReachProductsRepository(reachProductsService, database)
	}
}
