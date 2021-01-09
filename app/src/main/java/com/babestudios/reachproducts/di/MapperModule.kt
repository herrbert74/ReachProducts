package com.babestudios.reachproducts.di

import com.babestudios.base.data.mapNullInputList
import com.babestudios.reachproducts.data.network.dto.ProductDto
import com.babestudios.reachproducts.data.network.dto.mapAppearance
import com.babestudios.reachproducts.data.network.dto.mapProductDto
import com.babestudios.reachproducts.data.network.dto.mapOccupation
import com.babestudios.reachproducts.model.Product
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Not used for now because there is a compile problem in Kotlin for Lists.
 * Instead it's defined directly in the repository class.
 */
@Module
@InstallIn(SingletonComponent::class)
class MapperModule {
	@Provides
	fun provideProductsDtoMapper(): (List<ProductDto>) -> List<Product> =
		{ list ->
			mapNullInputList(list) { productDto ->
				mapProductDto(
					productDto,
					::mapOccupation,
					::mapAppearance,
				)
			}
		}
}
