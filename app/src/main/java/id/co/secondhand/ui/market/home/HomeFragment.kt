package id.co.secondhand.ui.market.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.co.secondhand.R
import id.co.secondhand.databinding.FragmentHomeBinding
import id.co.secondhand.ui.adapter.MarketLoadStateAdapter
import id.co.secondhand.ui.adapter.ProductGridAdapter
import id.co.secondhand.ui.market.product.detail.DetailProductActivity
import id.co.secondhand.ui.market.product.detail.DetailProductActivity.Companion.EXTRA_ID

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var productAdapter: ProductGridAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        setupAdapter()
        observeResult()
    }

    private fun observeResult() {
        viewModel.product.observe(viewLifecycleOwner) {
            productAdapter.submitData(lifecycle, it)
        }
    }

    private fun setupAdapter() {
        productAdapter = ProductGridAdapter(::onClicked)
        binding.apply {
            productRv.layoutManager = GridLayoutManager(requireContext(), 2)
            productRv.isNestedScrollingEnabled = false
            productRv.adapter = productAdapter.withLoadStateFooter(
                footer = MarketLoadStateAdapter { productAdapter.retry() }
            )
        }
        productAdapter.addLoadStateListener { loadState ->
            binding.apply {
                progressCircular.isVisible = loadState.source.refresh is LoadState.Loading
                productRv.isVisible = loadState.source.refresh is LoadState.NotLoading
                buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                textViewError.isVisible = loadState.source.refresh is LoadState.Error

                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    productAdapter.itemCount < 1
                ) {
                    productRv.isVisible = false
                    textViewEmpty.isVisible = true
                } else {
                    textViewEmpty.isVisible = false
                }
            }
        }
    }

    private fun onClicked(productId: Int) {
        val direction = Intent(requireContext(), DetailProductActivity::class.java)
        direction.putExtra(EXTRA_ID, productId)
        startActivity(direction)
    }
}