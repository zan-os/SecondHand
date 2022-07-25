package id.co.secondhand.ui.market.product.bidderinfo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import id.co.secondhand.R
import id.co.secondhand.data.remote.response.OrderDtoItem
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.databinding.ActivityBidderInfoBinding
import id.co.secondhand.ui.market.product.productmatch.ProductMatchFragment
import id.co.secondhand.utils.Extension.currencyFormatter
import id.co.secondhand.utils.Extension.showSnackbar

@AndroidEntryPoint
class BidderInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBidderInfoBinding

    private val viewModel: BidderInfoViewModel by viewModels()

    private var order: OrderDtoItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBidderInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        arrowBack()
        getAccessToken()
    }

    private fun getAccessToken() {
        viewModel.token.observe(this) { token ->
            val orderId = intent.getIntExtra(ORDER_ID, 0)
            observeResult(token, orderId)
            setUpButton(token, orderId)
        }
    }

    private fun observeResult(token: String, orderId: Int) {
        viewModel.getOrderData(token, orderId)
            .observe(this) { result ->
                when (result) {
                    is Resource.Loading -> {
                        showLoading(true)
                    }
                    is Resource.Success -> {
                        order = result.data
                        showLoading(false)
                        checkStatus(result.data?.status)
                        showOrderData(result.data)
                    }
                    is Resource.Error -> {
                        showLoading(false)
                        showErrorMessage(result.message)
                    }
                }
            }
    }

    private fun checkStatus(status: String?) {
        binding.apply {
            if (status == "accepted") {
                acceptBtn.visibility = View.GONE
                rejectBtn.visibility = View.GONE
                openWhatsappBtn.visibility = View.VISIBLE
            } else if (status == "declined") {
                acceptBtn.visibility = View.GONE
                rejectBtn.visibility = View.GONE
                binding.openWhatsappBtn.visibility = View.GONE
            }
        }
    }

    private fun setUpButton(token: String, orderId: Int) {
        binding.acceptBtn.setOnClickListener {
            updateOrder(token, orderId, "accepted")
        }
        binding.rejectBtn.setOnClickListener {
            updateOrder(token, orderId, "declined")
        }
    }

    private fun updateOrder(token: String, orderId: Int, status: String) {
        viewModel.updateOrder(token, orderId, status)
            .observe(this) { result ->
                when (result) {
                    is Resource.Loading -> {
                        showLoading(true)
                    }
                    is Resource.Success -> {
                        showLoading(false)
                        showBottomSheet(order)
                    }
                    is Resource.Error -> {
                        showLoading(false)
                        showErrorMessage(result.message)
                    }
                }
            }
    }

    private fun showBottomSheet(order: OrderDtoItem?) {
        val bottomSheetDialog = ProductMatchFragment().newInstance(order)
        bottomSheetDialog.show(supportFragmentManager, "ProductMatchDialog")
    }

    private fun showOrderData(order: OrderDtoItem?) {
        if (order !== null) {
            binding.apply {
                statusTv.text = order.status
                buyerNameTv.text = order.userDto?.fullName
                cityTv.text = order.userDto?.city
                productNameTv.text = order.product?.name
                productPriceTv.text = order.product?.basePrice?.currencyFormatter()
                bargainPriceTv.text = getString(
                    R.string.data_ditawar,
                    order.price?.currencyFormatter()
                )
                Glide.with(this@BidderInfoActivity)
                    .load(order.product?.imageUrl)
                    .placeholder(R.drawable.ic_error_image)
                    .dontAnimate()
                    .dontTransform()
                    .into(productImageIv)
                Glide.with(this@BidderInfoActivity)
                    .load(order.userDto?.imageUrl)
                    .placeholder(R.drawable.ic_error_image)
                    .dontAnimate()
                    .dontTransform()
                    .into(profileImageIv)
            }
            callSeller(order)
        }
    }

    private fun showErrorMessage(message: String?) {
        message?.showSnackbar(
            view = binding.root,
            context = this,
            textColor = R.color.white,
            backgroundColor = R.color.alert_danger
        )
    }

    private fun callSeller(order: OrderDtoItem) {
        binding.openWhatsappBtn.setOnClickListener {
            val message =
                "Halo ${order.userDto?.fullName}, Kami telah menerima tawaran produk ${order.product?.name} dengan harga ${order.price?.currencyFormatter()}. Silahkan konfirmasi pembelian"
            val url = "https://api.whatsapp.com/send?phone=+62${order.userDto?.phoneNumber}&text=$message"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }

    private fun showLoading(visible: Boolean) {
        binding.progressCircular.isVisible = visible
    }

    private fun arrowBack() {
        binding.materialToolbar.setNavigationOnClickListener { finish() }
    }

    companion object {
        const val ORDER_ID = "orderId"
    }
}