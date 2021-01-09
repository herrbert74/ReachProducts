package com.babestudios.reachproducts.data.network.dto

import com.babestudios.reachproducts.model.Product

inline fun mapProductDto(
	input: ProductDto,
	occupationMapper: (List<String>) -> String,
	appearanceMapper: (List<Int>?) -> String
): Product {
	return Product(
		input.char_id,
		input.name,
		occupationMapper(input.occupation),
		input.img ?: "",
		input.status ?: Status.Unknown,
		input.nickname ?: "",
		appearanceMapper(input.appearance)
	)
}

fun mapOccupation(occupationList: List<String>): String {
	return occupationList.joinToString(", ")
}

fun mapAppearance(appearanceList: List<Int>?): String {
	return appearanceList?.joinToString(", ") ?: ""
}