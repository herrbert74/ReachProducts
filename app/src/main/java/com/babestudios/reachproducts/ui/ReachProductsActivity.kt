package com.babestudios.reachproducts.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import com.babestudios.reachproducts.R

@AndroidEntryPoint
class ReachProductsActivity : AppCompatActivity() {

    private val reachProductsViewModel: ReachProductsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reach_products)
        reachProductsViewModel
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        val navController = findNavController(R.id.navHostFragment)
        reachProductsViewModel.bindNavController(navController)
    }
}
