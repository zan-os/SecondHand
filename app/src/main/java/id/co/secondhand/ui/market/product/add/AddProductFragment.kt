package id.co.secondhand.ui.market.product.add

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import id.co.secondhand.ui.market.product.preview.PreviewProductActivity
import id.co.secondhand.ui.market.product.preview.PreviewProductActivity.Companion.EXTRA_PREVIEW
import id.co.secondhand.ui.market.product.preview.PreviewProductActivity.Companion.EXTRA_TOKEN
import id.co.secondhand.utils.CategoryList
import id.co.secondhand.utils.Extension.showSnackbar
import id.co.secondhand.utils.Extension.validateDescription
import java.io.File

@AndroidEntryPoint
class AddProductFragment : Fragment() {

    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddProductViewModel by viewModels()

    private var getFile: File? = null
    private var categoryId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddProductBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dropDownMenuInit()
        observeAccessToken()
        openGallery()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun dropDownMenuInit() {
        val categories = ArrayList<CategoryList>()
        categories.add(CategoryList("Aksesoris", "15"))
        categories.add(CategoryList("Hobi", "30"))
        categories.add(CategoryList("Kendaraan", "31"))
        categories.add(CategoryList("Elektronik", "75"))
        categories.add(CategoryList("Kesehatan", "33"))

        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, categories)
        binding.autoCompleteTv.setAdapter(arrayAdapter)
        binding.autoCompleteTv.setOnItemClickListener { adapterView: AdapterView<*>?, view: View?, pos: Int, l: Long ->
            run {
                val string: CategoryList = adapterView?.getItemAtPosition(pos) as CategoryList
                categoryId = string.tag as String
                Log.d("category", categoryId)
            }
        }
    }

    private fun openGallery() {
        binding.productImageContainer.setOnClickListener {
            TedImagePicker.with(requireContext())
                .start { uri ->
                    val file = uri.toFile()

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
            addProduct(token)
            previewProduct(token)
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
                            Log.d("Market", "Loading")
                        }
                        is Resource.Success -> {
                            showLoading(false)
                            Log.d("Market", result.data.toString())
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
                            Log.d("Market", "Error ${result.message}")
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
}