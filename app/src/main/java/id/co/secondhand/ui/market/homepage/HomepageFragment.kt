package id.co.secondhand.ui.market.homepage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.co.secondhand.databinding.FragmentHomepageBinding
import id.co.secondhand.ui.adapter.MarketLoadStateAdapter
import id.co.secondhand.ui.adapter.ProductGridAdapter
import id.co.secondhand.ui.market.product.detail.DetailProductActivity
import id.co.secondhand.ui.market.product.detail.DetailProductActivity.Companion.EXTRA_ID

@AndroidEntryPoint
class HomepageFragment : Fragment() {
    private var _binding: FragmentHomepageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomepageViewModel by viewModels()
    private lateinit var productAdapter: ProductGridAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomepageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeResult()
        setupAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeResult() {
//        viewModel.getProducts().observe(viewLifecycleOwner) { result ->
//            when (result) {
//                is Resource.Loading -> {
//                    Log.d("Market", "Loading")
//                    showLoading(true)
//                }
//                is Resource.Success -> {
//                    showLoading(false)
//                    Log.d("Market", result.data.toString())
//                    showProduct(result.data ?: emptyList())
//                }
//                is Resource.Error -> {
//                    showLoading(false)
//                    Log.d("Market", "Error ${result.message.toString()}")
//                }
//            }
//        }
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

                // empty view
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

    private fun showLoading(value: Boolean) {
        if (value) {
            binding.progressCircular.visibility = View.VISIBLE
        } else {
            binding.progressCircular.visibility = View.GONE
        }
    }

    private fun onClicked(productId: Int) {
        val direction = Intent(requireContext(), DetailProductActivity::class.java)
        direction.putExtra(EXTRA_ID, productId)
        startActivity(direction)
    }
}