package id.co.secondhand.ui.market.product.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import id.co.secondhand.R
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.databinding.ActivityDetailProductBinding
import id.co.secondhand.domain.model.buyer.Product
import id.co.secondhand.utils.Extension.currencyFormatter

@AndroidEntryPoint
class DetailProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailProductBinding

    private val viewModel: DetailProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val productId = intent.getIntExtra(EXTRA_ID, 0)
        observeResult(productId = productId)

        navigateBack()
    }

    private fun observeResult(productId: Int) {
        viewModel.getDetailProduct(productId = productId)
            .observe(this) { result ->
                when (result) {
                    is Resource.Loading -> {
                        showLoading(true)
                    }
                    is Resource.Success -> {
                        showLoading(false)
                        negotiate(result.data)
                        showProductData(result.data)
                    }
                    is Resource.Error -> {
                        showLoading(false)
                    }
                }
            }
    }

    private fun showLoading(visible: Boolean) {
        binding.progressCircular.isVisible = visible
    }

    private fun showProductData(detail: Product?) {
        binding.apply {
            if (detail?.user != null) {
                binding.apply {
                    sellerNameTv.text = detail.user.fullName
                    sellerCityTv.text = detail.user.city
                    productNameTv.text = detail.name
                    productPriceTv.text = detail.basePrice.currencyFormatter()
                    productDescTv.text = detail.description
                    Glide.with(this@DetailProductActivity)
                        .load(detail.imageUrl)
                        .placeholder(R.drawable.ic_error_image)
                        .dontAnimate()
                        .dontTransform()
                        .into(productImageIv)
                    Glide.with(this@DetailProductActivity)
                        .load(detail.user.imageUrl)
                        .placeholder(R.drawable.ic_error_image)
                        .dontAnimate()
                        .dontTransform()
                        .into(sellerImageIv)
                }
            }
        }
    }

    private fun navigateBack() {
        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun negotiate(product: Product?) {
        binding.bargainBtn.setOnClickListener {
            val bottomSheetDialog = NegotiateFragment().newInstance(product)
            bottomSheetDialog.show(supportFragmentManager, "BottomSheetDialog")
        }
    }

    companion object {
        var EXTRA_ID = "extra_id"
    }
}