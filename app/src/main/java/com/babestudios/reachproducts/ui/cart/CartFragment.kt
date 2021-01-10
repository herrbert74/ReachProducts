package com.babestudios.reachproducts.ui.cart


import android.os.Bundle
import android.view.*
import android.widget.TextView
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
import com.babestudios.reachproducts.databinding.FragmentCartBinding
import com.babestudios.reachproducts.model.CartEntry
import com.babestudios.reachproducts.ui.ReachProductsViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import com.xwray.groupie.groupiex.plusAssign
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.clicks

@AndroidEntryPoint
class CartFragment : Fragment() {

    private val reachProductsViewModel: ReachProductsViewModel by activityViewModels()

    private var _binding: FragmentCartBinding? = null
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
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUI()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.cart_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_pay) {
            reachProductsViewModel.payOrDeleteCart()
        }
        return true
    }

    private fun initializeUI() {
        (activity as AppCompatActivity).setSupportActionBar(binding.tbCart)
        createSearchRecyclerView()

        reachProductsViewModel.cartLiveData.observe(viewLifecycleOwner) { result ->
            showContent(result)
        }

        reachProductsViewModel.totalAmountLiveData.observe(viewLifecycleOwner) { totalAmount ->
            binding.lblCartTotalAmount.text = totalAmount
        }

        if (groupAdapter.itemCount == 0) {
            reachProductsViewModel.loadCart()
        }

        binding.btnCartReload.clicks().onEach {
            binding.llCartCannotConnect.visibility = View.GONE
            reachProductsViewModel.loadCart()
        }.launchIn(lifecycleScope)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createSearchRecyclerView() {
        val linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvCart.layoutManager = linearLayoutManager
        binding.rvCart.addItemDecoration(DividerItemDecoration(requireContext()))
        binding.rvCart.adapter = groupAdapter
        binding.rvCart.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                firstVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            }
        })
        binding.rvCart.scrollToPosition(firstVisibleItemPosition)
    }

    private fun showContent(cartEntries: List<CartEntry>) {
        if (cartEntries.isEmpty()) {
            binding.msvCart.viewState = VIEW_STATE_EMPTY
        } else {
            binding.msvCart.viewState = VIEW_STATE_CONTENT
            if (groupAdapter.itemCount == 0) {
                productsSection = Section()
                productsSection.apply {
                    for (element in cartEntries) {
                        add(CartItem(element))
                    }
                }
                groupAdapter.plusAssign(productsSection)
            } else {
                productsSection.update(cartEntries.map { CartItem(it) })
            }
            if (binding.rvCart.adapter == null) {
                binding.rvCart.adapter = groupAdapter
            }
        }
    }

    private fun showError(throwable: Throwable) {
        if (throwable is OfflineException) {
            showContent((throwable.obj as? List<CartEntry>) ?: emptyList())
            binding.llCartCannotConnect.visibility = View.VISIBLE
        } else {
            val tvMsvSearchError = binding.msvCart.findViewById<TextView>(R.id.tvMsvError)
            binding.msvCart.viewState = VIEW_STATE_ERROR
            tvMsvSearchError.text = throwable.message
        }
    }
}
