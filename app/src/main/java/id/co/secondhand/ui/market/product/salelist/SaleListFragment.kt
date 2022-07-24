package id.co.secondhand.ui.market.product.salelist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import id.co.secondhand.R
import id.co.secondhand.data.remote.response.OrderDtoItem
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.databinding.FragmentSaleListBinding
import id.co.secondhand.domain.model.auth.User
import id.co.secondhand.domain.model.buyer.Product
import id.co.secondhand.ui.adapter.OrderListAdapter
import id.co.secondhand.ui.adapter.ProductGridAdapter
import id.co.secondhand.ui.auth.login.LoginActivity
import id.co.secondhand.ui.market.product.detail.DetailProductActivity
import id.co.secondhand.ui.market.product.detail.DetailProductActivity.Companion.EXTRA_ID
import id.co.secondhand.ui.market.profile.update.EditProfileActivity
import id.co.secondhand.utils.Extension
import id.co.secondhand.utils.Extension.showSnackbar

@AndroidEntryPoint
class SaleListFragment : Fragment() {

    private var _binding: FragmentSaleListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SaleListViewModel by viewModels()

    private val gridAdapter: ProductGridAdapter by lazy { ProductGridAdapter(::navigateToDetail) }
    private val listAdapter: OrderListAdapter by lazy { OrderListAdapter(::onClick) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSaleListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAccessToken()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getAccessToken() {
        viewModel.token.observe(viewLifecycleOwner) { token ->
            credentialCheck(token)
        }
    }

    private fun credentialCheck(token: String) {
        if (token.isEmpty()) {
            binding.notLoggedInLayout.root.visibility = View.VISIBLE
            binding.saleListLayout.visibility = View.GONE
            navigateToLogin()
        } else {
            binding.notLoggedInLayout.root.visibility = View.GONE
            binding.saleListLayout.visibility = View.VISIBLE
            getSaleProduct(token)
            getOrder(token)
            getOrderHistory(token)
            getUserData(token)
            showSaleProduct(token)
        }
    }

    private fun getUserData(token: String) {
        viewModel.getUserData(token).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    Log.d("Market", "Loading")
                    showLoading(true)
                }
                is Resource.Success -> {
                    showLoading(false)
                    Log.d("Market", result.data.toString())
                    result.data?.let {
                        showUserData(it)
                        navigateToEditProfile(it)
                    }
                }
                is Resource.Error -> {
                    showLoading(false)
                    Log.d("Market", "Error ${result.message.toString()}")
                }
            }
        }
    }

    private fun getSaleProduct(token: String) {
        viewModel.getSaleProduct(token).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    Log.d("Market", "Loading")
                    showLoading(true)
                }
                is Resource.Success -> {
                    showLoading(false)
                    Log.d("Market", result.data.toString())
                    showProduct(result.data)
                }
                is Resource.Error -> {
                    showLoading(false)
                    Log.d("Market", "Error ${result.message.toString()}")
                    showErrorMessage(result.message, binding.root)
                }
            }
        }
    }

    private fun showSaleProduct(token: String) {
        binding.productBtn.setOnClickListener {
            getSaleProduct(token)
        }
    }

    private fun getOrder(token: String) {
        binding.interestedBtn.setOnClickListener {
            viewModel.getOrder(token, "pending").observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Resource.Loading -> {
                        Log.d("Market", "Loading")
                        showLoading(true)
                    }
                    is Resource.Success -> {
                        showLoading(false)
                        Log.d("Market", result.data.toString())
                        showOrder(result.data)
                    }
                    is Resource.Error -> {
                        showLoading(false)
                        Log.d("Market", "Error ${result.message.toString()}")
                        showErrorMessage(result.message, binding.root)
                    }
                }
            }
        }
    }

    private fun getOrderHistory(token: String) {
        binding.soldBtn.setOnClickListener {
            viewModel.getOrder(token, "accepted").observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Resource.Loading -> {
                        Log.d("Market", "Loading")
                        showLoading(true)
                    }
                    is Resource.Success -> {
                        showLoading(false)
                        Log.d("Market", result.data.toString())
                        showOrder(result.data)
                    }
                    is Resource.Error -> {
                        showLoading(false)
                        Log.d("Market", "Error ${result.message.toString()}")
                        showErrorMessage(result.message, binding.root)
                    }
                }
            }
        }
    }

    private fun showUserData(user: User) {
        binding.apply {
            sellerNameTv.text = user.fullName
            cityTv.text = user.city
            Glide.with(requireContext())
                .load(user.imageUrl)
                .placeholder(R.drawable.ic_error_image)
                .into(profileImageIv)
        }
    }

    private fun showProduct(product: List<Product>?) {
        if (product?.isEmpty() == true) {
            binding.productRv.visibility = View.GONE
            binding.emptyListTv.visibility = View.VISIBLE
            binding.noOrderIv.visibility = View.VISIBLE
            binding.emptyListTv.text = getString(R.string.anda_tidak_memiliki_barang_dagangan)
        } else {
            binding.emptyListTv.visibility = View.GONE
            binding.noOrderIv.visibility = View.GONE
            binding.productRv.visibility = View.VISIBLE
//            gridAdapter.submitList(product)
            binding.productRv.layoutManager =
                GridLayoutManager(requireContext(), 2)
            binding.productRv.isNestedScrollingEnabled = false
            binding.productRv.adapter = gridAdapter
        }
    }

    private fun showOrder(product: List<OrderDtoItem>?) {
        if (product?.isEmpty() == true) {
            binding.productRv.visibility = View.GONE
            binding.noOrderIv.visibility = View.VISIBLE
            binding.emptyListTv.visibility = View.VISIBLE
        } else {
            binding.emptyListTv.visibility = View.GONE
            binding.noOrderIv.visibility = View.GONE
            binding.productRv.visibility = View.VISIBLE
            listAdapter.submitList(product)
            binding.productRv.layoutManager = LinearLayoutManager(requireContext())
            binding.productRv.isNestedScrollingEnabled = false
            binding.productRv.adapter = listAdapter
        }
    }

    private fun showLoading(value: Boolean) {
        if (value) {
            binding.progressCircular.visibility = View.VISIBLE
        } else {
            binding.progressCircular.visibility = View.GONE
        }
    }

    private fun onClick(productId: Int) {

    }

    private fun navigateToDetail(productId: Int) {
        val direction = Intent(requireContext(), DetailProductActivity::class.java)
        direction.putExtra(EXTRA_ID, productId)
        startActivity(direction)
    }

    private fun navigateToEditProfile(user: User) {
        binding.editProfileBtn.setOnClickListener {
            val direction = Intent(requireContext(), EditProfileActivity::class.java)
            direction.putExtra(Extension.EXTRA_USER, user)
            startActivity(direction)
        }
    }

    private fun showErrorMessage(code: String?, view: View) {
        when (code) {
            "403" -> {
                "Anda belum login".showSnackbar(
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
}