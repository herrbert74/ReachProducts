package com.babestudios.reachproducts.model

import com.babestudios.reachproducts.data.network.dto.Status

data class Product(
	val id: Long = 0L,
	val name: String = "",
	val occupation: String = "",
	val img: String = "",
	val status: Status = Status.Unknown,
	val nickname: String = "",
	val appearance: String = "",
)
