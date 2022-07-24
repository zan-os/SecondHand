package id.co.secondhand.ui.market.homepage

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
import id.co.secondhand.databinding.FragmentHomepageBinding
import id.co.secondhand.ui.adapter.MarketLoadStateAdapter
import id.co.secondhand.ui.adapter.ProductGridAdapter
import id.co.secondhand.ui.market.product.detail.DetailProductActivity
import id.co.secondhand.ui.market.product.detail.DetailProductActivity.Companion.EXTRA_ID

@AndroidEntryPoint
class HomepageFragment : Fragment(R.layout.fragment_homepage) {
    private var _binding: FragmentHomepageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomepageViewModel by viewModels()
    private lateinit var productAdapter: ProductGridAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomepageBinding.bind(view)

        setupAdapter()
        observeResult()
        categoryButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeResult() {
        viewModel.product.observe(viewLifecycleOwner) {
            productAdapter.submitData(lifecycle, it)
        }
    }

    private fun searchByCategory(categoryId: Int?) {
        viewModel.searchByCategory(categoryId).observe(viewLifecycleOwner) {
            productAdapter.submitData(lifecycle, it)
        }
    }

    private fun categoryButton() {
        binding.allProductBtn.setOnClickListener {
            searchByCategory(null)
        }
        binding.accessoriesBtn.setOnClickListener {
            searchByCategory(7)
        }
        binding.electronicBtn.setOnClickListener {
            searchByCategory(1)
        }
        binding.hobbyBtn.setOnClickListener {
            searchByCategory(9)
        }
        binding.handphoneBtn.setOnClickListener {
            searchByCategory(3)
        }
        binding.healthBtn.setOnClickListener {
            searchByCategory(8)
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