package id.co.secondhand.ui.market.product.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import id.co.secondhand.R
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.databinding.ActivityDetailProductBinding
import id.co.secondhand.domain.model.buyer.DetailProduct
import id.co.secondhand.ui.MainActivity
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

        navigateToHomepage()
        negotiate(productId)
    }


    private fun observeResult(productId: Int) {
        viewModel.getDetailProduct(productId = productId)
            .observe(this) { result ->
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
                Glide.with(this@DetailProductActivity)
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
            val direction = Intent(this, MainActivity::class.java)
            startActivity(direction)
            finish()
        }
    }

    companion object {
        var EXTRA_ID = "extra_id"
    }

    private fun negotiate(productId: Int) {
        binding.bargainBtn.setOnClickListener {
            val bottomSheetDialog = NegotiateFragment()
            val bundle = Bundle()
            bundle.putInt("productId", productId)
            bottomSheetDialog.arguments = bundle
            bottomSheetDialog.show(supportFragmentManager, "BottomSheetDialog")
        }
    }
}