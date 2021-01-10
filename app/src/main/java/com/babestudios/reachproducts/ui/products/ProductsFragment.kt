package com.babestudios.reachproducts.ui.products


import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.babestudios.base.network.OfflineException
import com.babestudios.base.view.DividerItemDecoration
import com.babestudios.base.view.MultiStateView.*
import com.babestudios.reachproducts.R
import com.babestudios.reachproducts.databinding.FragmentProductsBinding
import com.babestudios.reachproducts.model.ProductsResponse
import com.babestudios.reachproducts.ui.ReachProductsViewModel
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import com.xwray.groupie.groupiex.plusAssign
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.clicks

@AndroidEntryPoint
class ProductsFragment : Fragment() {

    private val reachProductsViewModel: ReachProductsViewModel by activityViewModels()

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!

    private var groupAdapter = GroupAdapter<GroupieViewHolder>()

    private var productsSection = Section()

    var firstVisibleItemPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.products_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_cart) {
            reachProductsViewModel.navigateToProductDetails()
        }
        return true
    }

    private fun initializeUI() {
        (activity as AppCompatActivity).setSupportActionBar(binding.tbProducts)
        createSearchRecyclerView()

        reachProductsViewModel.productsLiveData.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                showContent(it)
            }
            result.onFailure {
                showError(it)
            }
        }

        groupAdapter.setOnItemClickListener { item, _ ->
            val product = (item as ProductItem).product
            reachProductsViewModel.addToCart(product)
            Toast.makeText(
                requireContext(),
                String.format(getString(R.string.product_added), product.name),
                Toast.LENGTH_SHORT
            ).show()
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

    private fun showContent(productsResponse: ProductsResponse) {
        binding.msvProducts.viewState = VIEW_STATE_CONTENT
        if (groupAdapter.itemCount == 0) {
            productsSection = Section()
            productsSection.apply {
                for (element in productsResponse.products) {
                    add(ProductItem(element))
                }
            }
            groupAdapter.plusAssign(productsSection)
        } else {
            productsSection.update(productsResponse.products.map { ProductItem(it) })
        }
        if (binding.rvProducts.adapter == null) {
            binding.rvProducts.adapter = groupAdapter
        }
    }

    private fun showError(throwable: Throwable) {
        if (throwable is OfflineException) {
            showContent((throwable.obj as? ProductsResponse) ?: ProductsResponse(emptyList()))
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
