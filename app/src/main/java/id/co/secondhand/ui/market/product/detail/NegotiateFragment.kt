package id.co.secondhand.ui.market.product.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import id.co.secondhand.R
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.databinding.FragmentNegotiateBinding
import id.co.secondhand.domain.model.buyer.DetailProduct
import id.co.secondhand.utils.Extension.currencyFormatter

@AndroidEntryPoint
class NegotiateFragment : BottomSheetDialogFragment() {

    private var _bindig: FragmentNegotiateBinding? = null
    private val binding get() = _bindig!!

    private val viewModel: NegotiateViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bindig = FragmentNegotiateBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = arguments
        val productId = data?.get("productId")

        observeResult(productId = productId as Int)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _bindig = null
    }

    private fun observeResult(productId: Int) {
        viewModel.getDetailProduct(productId = productId)
            .observe(this) { result ->
                when (result) {
                    is Resource.Loading -> {
                        Log.d("Market", "Loading")
                    }
                    is Resource.Success -> {
                        Log.d("Market", result.data.toString())
                        showProductData(result.data)
                    }
                    is Resource.Error -> {
                        Log.d("Market", "Error ${result.message.toString()}")
                    }
                }
            }
    }

    private fun showProductData(detail: DetailProduct?) {
        binding.apply {
            if (detail != null) {
                binding.tvProductName.text = detail.name
                binding.tvProductPrice.text = detail.basePrice?.currencyFormatter()
                Glide.with(this@NegotiateFragment)
                    .load(detail.imageUrl)
                    .placeholder(R.drawable.ic_error_image)
                    .dontAnimate()
                    .dontTransform()
                    .into(binding.ivProduct)
            }
        }
    }

}