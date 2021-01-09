package com.babestudios.reachproducts.data.res

import android.content.Context
import com.babestudios.base.di.qualifier.ApplicationContext
import com.babestudios.reachproducts.R
import javax.inject.Inject
import javax.inject.Singleton


interface StringResourceHelperContract {
	fun getDueString(): String
}

@Singleton
class StringResourceHelper @Inject constructor(@ApplicationContext val context: Context)
	: StringResourceHelperContract {

	override fun getDueString(): String {
		return context.getString(R.string.due)
	}

}
