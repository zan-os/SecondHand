package id.co.secondhand.ui.market.product.productmatch

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import id.co.secondhand.R
import id.co.secondhand.data.remote.response.OrderDtoItem
import id.co.secondhand.databinding.FragmentProductMatchBinding
import id.co.secondhand.utils.Constants
import id.co.secondhand.utils.Extension.currencyFormatter

@AndroidEntryPoint
class ProductMatchFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentProductMatchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProductMatchBinding.inflate(layoutInflater)
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
        order: OrderDtoItem?
    ): ProductMatchFragment {
        val args = Bundle()
        args.putParcelable(EXTRA_ORDER, order)
        val fragment = ProductMatchFragment()
        fragment.arguments = args
        return fragment
    }

    private fun getData() {
        val order = arguments?.getParcelable(Constants.EXTRA_PRODUCT) as OrderDtoItem?
        showOrder(order)
    }

    private fun showOrder(order: OrderDtoItem?) {
        if (order != null) {
            binding.apply {
                tvBuyerName.text = order.userDto?.fullName
                tvCity.text = order.userDto?.city
                Glide.with(requireContext())
                    .load(order.userDto?.imageUrl)
                    .placeholder(R.drawable.ic_error_image)
                    .dontAnimate()
                    .dontTransform()
                    .into(imageViewBuyer)
                tvProductName.text = order.product?.name
                tvProductPrice.text = order.product?.basePrice?.currencyFormatter()
                tvPriceNegotiate.text = getString(
                    R.string.data_ditawar,
                    order.price?.currencyFormatter()
                )
                Glide.with(requireContext())
                    .load(order.product?.imageUrl)
                    .placeholder(R.drawable.ic_error_image)
                    .dontAnimate()
                    .dontTransform()
                    .into(imageViewProduct)
            }

            callSeller(order)
        }
    }

    private fun callSeller(order: OrderDtoItem) {
        binding.openWhatsappBtn.setOnClickListener {
            val message =
                "Halo ${order.userDto?.fullName}, Kami telah menerima tawaran produk ${order.product?.name} dengan harga ${order.price?.currencyFormatter()}. Silahkan konfirmasi pembelian"
            val url =
                "https://api.whatsapp.com/send?phone=+62${order.userDto?.phoneNumber}&text=$message"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }

    companion object {
        const val EXTRA_ORDER = "extraOrder"
    }
}