package id.co.secondhand.ui.market.product.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import id.co.secondhand.R
import id.co.secondhand.data.remote.request.product.BargainRequest
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.databinding.FragmentNegotiateBinding
import id.co.secondhand.domain.model.buyer.Product
import id.co.secondhand.utils.Extension.currencyFormatter
import id.co.secondhand.utils.Extension.showSnackbar

@AndroidEntryPoint
class NegotiateFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentNegotiateBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NegotiateViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNegotiateBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun newInstance(
        product: Product?
    ): NegotiateFragment {
        val args = Bundle()
        args.putParcelable(EXTRA_PRODUCT, product)
        val fragment = NegotiateFragment()
        fragment.arguments = args
        return fragment
    }

    private fun getData() {
        val product = arguments?.getParcelable(EXTRA_PRODUCT) as Product?
        showProduct(product)
        getAccessToken(product?.id!!)
    }

    private fun getAccessToken(productId: Int) {
        viewModel.accessToken.observe(viewLifecycleOwner) { token ->
            if (token.isEmpty()) {
                binding.sendNegotiateBtn.text = getString(R.string.silahkan_login)
            } else {
                binding.sendNegotiateBtn.isEnabled = true
                bidProduct(token, productId)
            }
        }
    }

    private fun showProduct(product: Product?) {
        if (product != null) {
            binding.tvProductName.text = product.name
            binding.tvProductPrice.text = product.basePrice.currencyFormatter()
            Glide.with(this)
                .load(product.imageUrl)
                .placeholder(R.drawable.ic_error_image)
                .dontAnimate()
                .dontTransform()
                .into(binding.imageView)
        }
    }

    private fun bidProduct(token: String, productId: Int) {
        binding.sendNegotiateBtn.setOnClickListener {
            val bidPrice = binding.etNegotiatePrice.text.toString()
            val bargainRequest = BargainRequest(
                productId = productId,
                bidPrice = bidPrice
            )

            when {
                bidPrice.isEmpty() -> {
                    binding.etNegotiatePrice.error = "Masukan harga tawar"
                }
                else -> {
                    viewModel.bargainProduct(token, bargainRequest)
                        .observe(viewLifecycleOwner) { result ->
                            when (result) {
                                is Resource.Loading -> {
                                    showLoading(true)
                                }
                                is Resource.Success -> {
                                    showLoading(false)
                                    showSuccessMessage()
                                }
                                is Resource.Error -> {
                                    showLoading(false)
                                    result.message?.let { showErrorMessage(it) }
                                }
                            }
                        }
                }
            }
        }
    }

    private fun showSuccessMessage() {
        getString(R.string.bargain_success).showSnackbar(
            view = binding.root,
            context = requireContext(),
            textColor = R.color.white,
            backgroundColor = R.color.alert_success
        )
    }

    private fun showLoading(visible: Boolean) {
        binding.progressCircular.isVisible = visible
    }

    private fun showErrorMessage(message: String) {
        when (message) {
            "400" -> {
                getString(R.string.error_has_order_product).showSnackbar(
                    view = binding.root,
                    context = requireContext(),
                    textColor = R.color.white,
                    backgroundColor = R.color.alert_danger
                )
            }
            "403" -> {
                getString(R.string.error_not_login).showSnackbar(
                    view = binding.root,
                    context = requireContext(),
                    textColor = R.color.white,
                    backgroundColor = R.color.alert_danger
                )
            }
            "500" -> {
                getString(R.string.error_internal_server).showSnackbar(
                    view = binding.root,
                    context = requireContext(),
                    textColor = R.color.white,
                    backgroundColor = R.color.alert_danger
                )
            }
            else -> {
                message.showSnackbar(
                    view = binding.root,
                    context = requireContext(),
                    textColor = R.color.white,
                    backgroundColor = R.color.alert_danger
                )
            }
        }
    }

    companion object {
        const val EXTRA_PRODUCT = "extraProduct"
    }
}