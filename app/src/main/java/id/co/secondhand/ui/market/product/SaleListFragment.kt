package id.co.secondhand.ui.market.product

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
import id.co.secondhand.data.remote.response.auth.UserDataDto
import id.co.secondhand.data.remote.response.seller.OrderDtoItem
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.databinding.FragmentSaleListBinding
import id.co.secondhand.domain.model.Product
import id.co.secondhand.ui.adapter.OrderListAdapter
import id.co.secondhand.ui.adapter.ProductGridAdapter
import id.co.secondhand.ui.market.profile.update.EditProfileActivity

@AndroidEntryPoint
class SaleListFragment : Fragment() {

    private var _binding: FragmentSaleListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SaleListViewModel by viewModels()

    private val gridAdapter: ProductGridAdapter by lazy { ProductGridAdapter(::onClicked) }
    private val listAdapter: OrderListAdapter by lazy { OrderListAdapter(::onClicked) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSaleListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAccessToken()
        navigateToEditProfile()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getAccessToken() {
        viewModel.token.observe(viewLifecycleOwner) { token ->
            getSaleProduct(token)
            getOrder(token)
            getUserData(token)
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
                    showUserData(result.data)
                }
                is Resource.Error -> {
                    showLoading(false)
                    Log.d("Market", "Error ${result.message.toString()}")
                }
            }
        }
    }

    private fun getSaleProduct(token: String) {
        binding.productBtn.setOnClickListener {
            viewModel.getSaleProduct(token).observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Resource.Loading -> {
                        Log.d("Market", "Loading")
                        showLoading(true)
                    }
                    is Resource.Success -> {
                        showLoading(false)
                        Log.d("Market", result.data.toString())
                        showProduct(result.data ?: emptyList())
                    }
                    is Resource.Error -> {
                        showLoading(false)
                        Log.d("Market", "Error ${result.message.toString()}")
                    }
                }
            }
        }
    }

    private fun getOrder(token: String) {
        binding.interestedBtn.setOnClickListener {
            viewModel.getOrder(token).observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Resource.Loading -> {
                        Log.d("Market", "Loading")
                        showLoading(true)
                    }
                    is Resource.Success -> {
                        showLoading(false)
                        Log.d("Market", result.data.toString())
                        showOrder(result.data ?: emptyList())
                    }
                    is Resource.Error -> {
                        showLoading(false)
                        Log.d("Market", "Error ${result.message.toString()}")
                    }
                }
            }
        }
    }

    private fun showUserData(user: UserDataDto?) {
        binding.apply {
            if (user != null) {
                sellerNameTv.text = user.fullName
                cityTv.text = user.city
                Glide.with(requireContext())
                    .load(user.imageUrl)
                    .placeholder(R.drawable.ic_error_image)
                    .into(profileImageIv)
            }
        }
    }

    private fun showProduct(product: List<Product>) {
        gridAdapter.submitList(product)
        binding.productRv.layoutManager =
            GridLayoutManager(requireContext(), 2)
        binding.productRv.isNestedScrollingEnabled = false
        binding.productRv.adapter = gridAdapter
    }

    private fun showOrder(product: List<OrderDtoItem>) {
        listAdapter.submitList(product)
        binding.productRv.layoutManager = LinearLayoutManager(requireContext())
        binding.productRv.isNestedScrollingEnabled = false
        binding.productRv.adapter = listAdapter
    }

    private fun showLoading(value: Boolean) {
        if (value) {
            binding.progressCircular.visibility = View.VISIBLE
        } else {
            binding.progressCircular.visibility = View.GONE
        }
    }

    private fun onClicked(productId: Int) {
    }

    private fun navigateToEditProfile() {
        binding.editProfileBtn.setOnClickListener {
            val direction = Intent(requireActivity(), EditProfileActivity::class.java)
            startActivity(direction)
        }
    }
}