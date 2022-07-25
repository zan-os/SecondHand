package id.co.secondhand.ui.market.product.preview

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import id.co.secondhand.R
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.databinding.ActivityPreviewProductBinding
import id.co.secondhand.domain.model.auth.User
import id.co.secondhand.domain.model.seller.Preview
import id.co.secondhand.utils.Constants.EXTRA_PREVIEW
import id.co.secondhand.utils.Constants.EXTRA_TOKEN
import id.co.secondhand.utils.Extension.currencyFormatter
import id.co.secondhand.utils.Extension.showSnackbar

@AndroidEntryPoint
class PreviewProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreviewProductBinding

    private val viewModel: PreviewProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getParcelableExtra(EXTRA_PREVIEW) as Preview?
        val token = intent.getStringExtra(EXTRA_TOKEN)

        if (data != null && token != null) {
            showPreview(data)
            addProduct(token, data)
            observeSellerData(token)
            navigateBack()
        }
    }

    private fun observeSellerData(token: String) {
        viewModel.getSellerData(token).observe(this) { result ->
            when (result) {
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Success -> {
                    showLoading(false)
                    showSellerData(result.data)
                }
                is Resource.Error -> {
                    showLoading(false)
                }
            }
        }
    }

    private fun showSellerData(seller: User?) {
        if (seller != null) {
            binding.sellerNameTv.text = seller.fullName
            binding.cityTv.text = seller.city
            Glide.with(this)
                .load(seller.imageUrl)
                .into(binding.sellerImageIv)
        }
    }

    private fun showPreview(product: Preview) {
        Glide.with(this)
            .load(product.image)
            .into(binding.productImageIv)
        binding.productNameTv.text = product.name
        binding.categoryTv.text = product.category
        binding.productPriceTv.text = product.price.toInt().currencyFormatter()
        binding.productDescTv.text = product.description

    }

    private fun addProduct(token: String, product: Preview) {
        binding.addProductBtn.setOnClickListener {
            val name = product.name
            val categoryId = product.categoryId
            val basePrice = product.price
            val description = product.description
            val location = product.location
            val image = product.image

            viewModel.addProduct(
                token,
                image,
                name,
                description,
                basePrice,
                categoryId,
                location
            ).observe(this) { result ->
                when (result) {
                    is Resource.Loading -> {
                        showLoading(true)
                    }
                    is Resource.Success -> {
                        showLoading(false)
                        "Berhasil menambahkan barang".showSnackbar(
                            binding.root,
                            this,
                            R.color.white,
                            R.color.alert_success
                        )
                    }
                    is Resource.Error -> {
                        showLoading(false)
                        showErrorMessage(result.message, binding.root)
                    }
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

    private fun navigateBack() {
        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun showErrorMessage(code: String?, view: View) {
        when (code) {
            "400" -> {
                "Anda sudah melebihi batas kuota upload".showSnackbar(
                    view = view,
                    context = this,
                    textColor = R.color.white,
                    backgroundColor = R.color.alert_danger
                )
            }
            "403" -> {
                "Silahkan login terlebih dahulu".showSnackbar(
                    view = view,
                    context = this,
                    textColor = R.color.white,
                    backgroundColor = R.color.alert_danger
                )
            }
            "500" -> {
                "Internal Server Error :(".showSnackbar(
                    view,
                    this,
                    R.color.white,
                    R.color.alert_danger
                )
            }
            "503" -> {
                "Service Unavailable".showSnackbar(
                    view,
                    this,
                    R.color.white,
                    R.color.alert_danger
                )
            }
        }
    }
}