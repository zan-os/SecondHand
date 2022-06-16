package id.co.secondhand.ui.market.homepage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.databinding.FragmentHomepageBinding
import id.co.secondhand.domain.model.Product
import id.co.secondhand.ui.adapter.ProductGridAdapter

@AndroidEntryPoint
class HomepageFragment : Fragment() {
    private var _binding: FragmentHomepageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomepageViewModel by viewModels()

    private lateinit var adapter: ProductGridAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomepageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeToken()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeToken() {
        viewModel.token.observe(viewLifecycleOwner) {
            Log.d("Token", "Token = $it")
            observeResult(it)
            viewModel.accessToken.postValue(it)
        }
    }

    private fun observeResult(token: String) {
        viewModel.getProducts(token).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    Log.d("Market", "Loading")
                    showLoading(true)
                }
                is Resource.Success -> {
                    showLoading(false)
                    Log.d("Market", result.data.toString())
                    showProduct(result.data ?: emptyList())
                }
                is Resource.Error -> {
                    showLoading(false)
                    Log.d("Market", "Error ${result.message.toString()}")
                }
            }
        }
    }

    private fun showProduct(product: List<Product>) {
        adapter = ProductGridAdapter()
        adapter.submitList(product)
        binding.productRv.layoutManager =
            GridLayoutManager(requireContext(), 2)
        binding.productRv.isNestedScrollingEnabled = false
        binding.productRv.adapter = adapter
    }

    private fun showLoading(value: Boolean) {
        if (value) {
            binding.progressCircular.visibility = View.VISIBLE
        } else {
            binding.progressCircular.visibility = View.GONE
        }
    }
}