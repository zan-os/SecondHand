package id.co.secondhand.ui.market.product.add

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import gun0912.tedimagepicker.builder.TedImagePicker
import id.co.secondhand.R
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.databinding.FragmentAddProductBinding
import id.co.secondhand.domain.model.seller.Preview
import id.co.secondhand.ui.auth.login.LoginActivity
import id.co.secondhand.ui.market.product.preview.PreviewProductActivity
import id.co.secondhand.utils.CategoryList
import id.co.secondhand.utils.Constants.EXTRA_PREVIEW
import id.co.secondhand.utils.Constants.EXTRA_TOKEN
import id.co.secondhand.utils.Extension.showSnackbar
import id.co.secondhand.utils.Extension.uriToFile
import id.co.secondhand.utils.Extension.validateDescription
import java.io.File

@AndroidEntryPoint
class AddProductFragment : Fragment(R.layout.fragment_add_product) {

    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddProductViewModel by viewModels()

    private var getFile: File? = null
    private var categoryId: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddProductBinding.bind(view)

        dropDownMenuInit()
        observeAccessToken()
        openGallery()
    }

    private fun credentialCheck(token: String) {
        if (token.isEmpty()) {
            binding.notLoggedInLayout.root.visibility = View.VISIBLE
            binding.saleListLayout.visibility = View.GONE
            navigateToLogin()
        } else {
            binding.notLoggedInLayout.root.visibility = View.GONE
            binding.saleListLayout.visibility = View.VISIBLE
            addProduct(token)
            previewProduct(token)
        }
    }

    private fun dropDownMenuInit() {
        val categories = ArrayList<CategoryList>()
        categories.add(CategoryList("Elektronik", "1"))
        categories.add(CategoryList("Komputer dan Aksesoris", "2"))
        categories.add(CategoryList("Komputer dan Aksesoris", "3"))
        categories.add(CategoryList("Pakaian Pria", "4"))
        categories.add(CategoryList("sepatu Pria", "5"))
        categories.add(CategoryList("Tas Pria", "6"))
        categories.add(CategoryList("Aksesoris Fashion", "7"))
        categories.add(CategoryList("kesehatan", "8"))
        categories.add(CategoryList("Hobi dan Koleksi", "9"))
        categories.add(CategoryList("Makanan dan Minuman", "10"))
        categories.add(CategoryList("Perawatan dan Kecantikan", "11"))
        categories.add(CategoryList("Perlengkapan Rumah", "12"))
        categories.add(CategoryList("Pakaian Wanita", "13"))
        categories.add(CategoryList("Fashion Muslim", "14"))
        categories.add(CategoryList("Fashion Bayi dan Anak", "15"))
        categories.add(CategoryList("Ibu dan Bayi", "16"))
        categories.add(CategoryList("Sepatu Wanita", "17"))
        categories.add(CategoryList("Tas Wanita", "18"))
        categories.add(CategoryList("Otomotif", "19"))
        categories.add(CategoryList("Olahraga dan Outdoor", "20"))
        categories.add(CategoryList("Buku dan Alat Tulis", "21"))
        categories.add(CategoryList("Voucher", "22"))
        categories.add(CategoryList("Souvenir dan Pesta", "23"))
        categories.add(CategoryList("Fotografi", "24"))

        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, categories)
        binding.autoCompleteTv.setAdapter(arrayAdapter)
        binding.autoCompleteTv.setOnItemClickListener { adapterView: AdapterView<*>?, _: View?, pos: Int, _: Long ->
            run {
                val string: CategoryList = adapterView?.getItemAtPosition(pos) as CategoryList
                categoryId = string.tag as String
            }
        }
    }

    private fun openGallery() {
        binding.productImageContainer.setOnClickListener {
            TedImagePicker.with(requireContext())
                .start { uri ->
                    val file = uriToFile(uri, requireContext())

                    getFile = file

                    Glide.with(this)
                        .load(uri)
                        .override(300)
                        .centerCrop()
                        .into(binding.productImageIv)
                }
        }

    }

    private fun observeAccessToken() {
        viewModel.accessToken.observe(viewLifecycleOwner) { token ->
            credentialCheck(token)
        }
    }

    private fun addProduct(accessToken: String) {
        binding.addProductBtn.setOnClickListener {
            val name = binding.nameEt.text.toString()
            val desc = binding.descriptionEt.text.toString()
            val basePrice = binding.priceEt.text.toString()
            val categoryId = categoryId
            val location = binding.locationEt.text.toString()

            if (formValidator(name, desc, basePrice, categoryId, location)) {
                viewModel.addProduct(
                    accessToken,
                    getFile as File,
                    name,
                    desc,
                    basePrice,
                    categoryId,
                    location
                ).observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is Resource.Loading -> {
                            showLoading(true)
                        }
                        is Resource.Success -> {
                            showLoading(false)
                            "Berhasil menambahkan barang".showSnackbar(
                                binding.root,
                                requireContext(),
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
    }

    private fun previewProduct(token: String) {
        binding.previewProductBtn.setOnClickListener {
            val name = binding.nameEt.text.toString()
            val desc = binding.descriptionEt.text.toString()
            val basePrice = binding.priceEt.text.toString()
            val category = binding.autoCompleteTv.text.toString()
            val location = binding.locationEt.text.toString()

            if (formValidator(name, desc, basePrice, categoryId, location)) {
                val data = Preview(
                    name = name,
                    categoryId = categoryId,
                    category = category,
                    price = basePrice,
                    description = desc,
                    location = location,
                    image = getFile as File
                )

                val destination = Intent(requireContext(), PreviewProductActivity::class.java)
                destination.putExtra(EXTRA_TOKEN, token)
                destination.putExtra(EXTRA_PREVIEW, data)
                startActivity(destination)
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

    private fun formValidator(
        name: String,
        desc: String,
        basePrice: String,
        categoryId: String,
        location: String
    ): Boolean {
        when {
            name.isEmpty() -> {
                binding.nameEt.error = "Masukan nama barang"
            }
            basePrice.isEmpty() -> {
                binding.priceEt.error = "Masukan harga jual"
            }
            categoryId.isEmpty() -> {
                binding.autoCompleteTv.error = "Pilih kategori"
            }
            location.isEmpty() -> {
                binding.locationEt.error = "Masukan lokasi anda"
            }
            !desc.validateDescription() -> {
                binding.descriptionEt.error = "Deskripsi terlalu pendek"
            }
            getFile == null -> {
                "Silahkan upload gambar produk".showSnackbar(
                    binding.root,
                    requireContext(),
                    R.color.white,
                    R.color.alert_danger
                )
            }
            else -> {
                return true
            }
        }
        return false
    }

    private fun showErrorMessage(code: String?, view: View) {
        when (code) {
            "400" -> {
                "Sudah melebihi kuota upload".showSnackbar(
                    view = view,
                    context = requireContext(),
                    textColor = R.color.white,
                    backgroundColor = R.color.alert_danger
                )
            }
            "403" -> {
                "Silahkan login terlebih dahulu".showSnackbar(
                    view = view,
                    context = requireContext(),
                    textColor = R.color.white,
                    backgroundColor = R.color.alert_danger
                )
            }
            "500" -> {
                "Internal Server Error :(".showSnackbar(
                    view,
                    requireContext(),
                    R.color.white,
                    R.color.alert_danger
                )
            }
            "503" -> {
                "Service Unavailable".showSnackbar(
                    view,
                    requireContext(),
                    R.color.white,
                    R.color.alert_danger
                )
            }
        }
    }

    private fun navigateToLogin() {
        binding.notLoggedInLayout.loginBtn.setOnClickListener {
            val direction = Intent(requireContext(), LoginActivity::class.java)
            startActivity(direction)
            requireActivity()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}