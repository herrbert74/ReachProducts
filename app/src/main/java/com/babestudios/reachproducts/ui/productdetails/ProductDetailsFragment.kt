package com.babestudios.reachproducts.ui.productdetails


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
import com.babestudios.reachproducts.databinding.FragmentProductDetailsBinding
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
class ProductDetailsFragment : Fragment() {

    private val reachProductsViewModel: ReachProductsViewModel by activityViewModels()

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!

    private var groupAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            _binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUI()
    }

    private fun initializeUI() {
        (activity as AppCompatActivity).setSupportActionBar(binding.tbProductDetails)
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
            reachProductsViewModel.navigateToProductDetails((item as ProductDetailsItem).product.id)
        }

        if (groupAdapter.itemCount == 0) {
            reachProductsViewModel.loadProductDetails()
        }

        binding.btnProductDetailsReload.clicks().onEach {
            binding.llProductDetailsCannotConnect.visibility = View.GONE
            reachProductsViewModel.loadProductDetails()
        }.launchIn(lifecycleScope)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createSearchRecyclerView() {
        val linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvProductDetails.layoutManager = linearLayoutManager
        binding.rvProductDetails.addItemDecoration(DividerItemDecoration(requireContext()))
        binding.rvProductDetails.adapter = groupAdapter
        binding.rvProductDetails.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            firstVisibleItemPosition =
                (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            }
        })
        binding.rvProductDetails.scrollToPosition(firstVisibleItemPosition)
    }

    private fun showContent(productList: List<Product>) {
        binding.msvProductDetails.viewState = VIEW_STATE_CONTENT
        if (groupAdapter.itemCount == 0) {
            productsSection = Section()
            productsSection.apply {
                for (element in productList) {
                    add(ProductItem(element))
                }
            }
            groupAdapter += productsSection
        } else {
            productsSection.update(productList.map { ProductDetailsItem(it) })
        }
        if (binding.rvProductDetails.adapter == null) {
            binding.rvProductDetails.adapter = groupAdapter
        }
    }

    private fun showError(throwable: Throwable) {
        if (throwable is OfflineException) {
            showContent((throwable.obj as? List<Product>) ?: emptyList())
            binding.llProductDetailsCannotConnect.visibility = View.VISIBLE
        } else {
            val tvMsvSearchError = binding.msvProductDetails.findViewById<TextView>(R.id.tvMsvError)
            binding.msvProductDetails.viewState = VIEW_STATE_ERROR
            tvMsvSearchError.text = throwable.message
        }
    }

    private fun showLoading() {
        binding.msvProductDetails.viewState = VIEW_STATE_LOADING
    }
}
