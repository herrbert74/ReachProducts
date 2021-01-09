package com.babestudios.reachproducts.ui.products


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.babestudios.base.ext.textColor
import com.babestudios.base.network.OfflineException
import com.babestudios.base.view.DividerItemDecoration
import com.babestudios.base.view.FilterAdapter
import com.babestudios.base.view.MultiStateView.*
import com.babestudios.reachproducts.ui.ReachProductsViewModel
import com.babestudios.reachproducts.R
import com.babestudios.reachproducts.databinding.FragmentProductsBinding
import com.babestudios.reachproducts.model.Product
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import com.xwray.groupie.groupiex.plusAssign
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.clicks
import reactivecircus.flowbinding.android.widget.itemSelections
import reactivecircus.flowbinding.android.widget.textChanges

@AndroidEntryPoint
class ProductsFragment : Fragment() {

    private val reachProductsViewModel: ReachProductsViewModel by activityViewModels()

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!

    private var groupAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            _binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUI()
    }

    private fun initializeUI() {
        (activity as AppCompatActivity).setSupportActionBar(binding.tbProducts)
        createSearchRecyclerView()

        reachProductsViewModel.productsLiveData.observe(requireActivity()) { result ->
            result.onSuccess {
                showContent(it)
            }
            result.onFailure {
                showError(it)
            }
        }

        groupAdapter.setOnItemClickListener { item, _ ->
            reachProductsViewModel.navigateToProductDetails((item as ProductItem).product.id)
        }

        if (groupAdapter.itemCount == 0) {
            reachProductsViewModel.loadProducts()
        }

        binding.btnProductsReload.clicks().onEach {
            binding.llProductsCannotConnect.visibility = View.GONE
            reachProductsViewModel.loadProducts()
        }.launchIn(lifecycleScope)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createSearchRecyclerView() {
        val linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvProducts.layoutManager = linearLayoutManager
        binding.rvProducts.addItemDecoration(DividerItemDecoration(requireContext()))
        binding.rvProducts.adapter = groupAdapter
        binding.rvProducts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            firstVisibleItemPosition =
                (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            }
        })
        binding.rvProducts.scrollToPosition(firstVisibleItemPosition)
    }

    private fun showContent(productList: List<Product>) {
        binding.msvProducts.viewState = VIEW_STATE_CONTENT
        if (groupAdapter.itemCount == 0) {
            productsSection = Section()
            productsSection.apply {
                for (element in productList) {
                    add(ProductItem(element))
                }
            }
            groupAdapter += productsSection
        } else {
            productsSection.update(productList.map { ProductItem(it) })
        }
        if (binding.rvProducts.adapter == null) {
            binding.rvProducts.adapter = groupAdapter
        }
    }

    private fun showError(throwable: Throwable) {
        if (throwable is OfflineException) {
            showContent((throwable.obj as? List<Product>) ?: emptyList())
            binding.llProductsCannotConnect.visibility = View.VISIBLE
        } else {
            val tvMsvSearchError = binding.msvProducts.findViewById<TextView>(R.id.tvMsvError)
            binding.msvProducts.viewState = VIEW_STATE_ERROR
            tvMsvSearchError.text = throwable.message
        }
    }

    private fun showLoading() {
        binding.msvProducts.viewState = VIEW_STATE_LOADING
    }
}
