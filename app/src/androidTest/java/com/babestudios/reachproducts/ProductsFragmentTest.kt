package com.babestudios.reachproducts

import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.babestudios.reachproducts.di.NetworkModule
import com.babestudios.reachproducts.ui.ReachProductsActivity
import com.schibsted.spain.barista.assertion.BaristaListAssertions.assertListItemCount
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.interaction.BaristaListInteractions
import com.schibsted.spain.barista.interaction.BaristaMenuClickInteractions.clickMenu
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@UninstallModules(NetworkModule::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ProductsFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun populateRecyclerViewTest() {
        launchActivity<ReachProductsActivity>()
        assertListItemCount(R.id.rvProducts, 3)
    }

    @Test
    fun startDetailsTest() {
        launchActivity<ReachProductsActivity>()
        BaristaListInteractions.clickListItem(R.id.rvProducts, 0)
        clickMenu(R.id.action_cart)
        assertDisplayed(R.id.lblCartItemName, "Express Lipstick")
    }
}