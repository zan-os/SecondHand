package id.co.secondhand.ui.market.product.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import id.co.secondhand.R
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.databinding.FragmentDetailProductBinding
import id.co.secondhand.domain.model.buyer.DetailProduct
import id.co.secondhand.utils.Extension.currencyFormatter

@AndroidEntryPoint
class DetailProductFragment : Fragment() {

    private var _binding: FragmentDetailProductBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailProductViewModel by viewModels()

    private val args: DetailProductFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailProductBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeToken()
        navigateToHomepage()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeToken() {
        viewModel.token.observe(viewLifecycleOwner) {
            Log.d("Token", "Token = $it")
            val productId = args.productId
            observeResult(token = it, productId = productId)
            viewModel.accessToken.postValue(it)
        }
    }

    private fun observeResult(token: String, productId: Int) {
        viewModel.getDetailProduct(token = token, productId = productId)
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Resource.Loading -> {
                        Log.d("Market", "Loading")
                        showLoading(true)
                    }
                    is Resource.Success -> {
                        showLoading(false)
                        Log.d("Market", result.data.toString())
                        showProductData(result.data)
                    }
                    is Resource.Error -> {
                        showLoading(false)
                        Log.d("Market", "Error ${result.message.toString()}")
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

    private fun showProductData(detail: DetailProduct?) {
        binding.apply {
            if (detail != null) {
                binding.productNameTv.text = detail.name
                binding.productPriceTv.text = detail.basePrice?.currencyFormatter()
                binding.productDescTv.text = detail.description ?: "Tidak ada deskripsi"
                Glide.with(requireContext())
                    .load(detail.imageUrl)
                    .placeholder(R.drawable.ic_error_image)
                    .dontAnimate()
                    .dontTransform()
                    .into(binding.productImageIv)
            }
        }
    }

    private fun navigateToHomepage() {
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {
        var EXTRA_ID = "extra_id"
    }
}