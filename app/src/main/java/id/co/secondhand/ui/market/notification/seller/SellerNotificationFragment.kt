package id.co.secondhand.ui.market.notification.seller

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.databinding.FragmentSellerNotificationBinding
import id.co.secondhand.domain.model.notification.Notification
import id.co.secondhand.ui.adapter.NotificationListAdapter

@AndroidEntryPoint
class SellerNotificationFragment : Fragment() {

    private var _binding: FragmentSellerNotificationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SellerNotificationViewModel by viewModels()

    private val listAdapter: NotificationListAdapter by lazy { NotificationListAdapter(::onClicked) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSellerNotificationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAccessToken()
    }

    private fun getAccessToken() {
        viewModel.token.observe(viewLifecycleOwner) { token ->
            getNotification(token)
        }
    }

    private fun getNotification(accessToken: String) {
        viewModel.getNotification(accessToken).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    showLoading(true)
                    Log.d("Market", "Loading")
                }
                is Resource.Success -> {
                    showLoading(false)
                    showNotification(result.data ?: emptyList())
                    Log.d("Market", result.data.toString())
                }
                is Resource.Error -> {
                    showLoading(false)
                    Log.d("Market", "Error ${result.message}")
                }
            }
        }
    }

//    private fun getOrder(accessToken: String) {
//        viewModel.getOrder(accessToken).observe(viewLifecycleOwner) { result ->
//            when (result) {
//                is Resource.Loading -> {
//                    showLoading(true)
//                    Log.d("Market", "Loading")
//                }
//                is Resource.Success -> {
//                    showLoading(false)
//                    showOrder(result.data ?: emptyList())
//                    Log.d("Market", result.data.toString())
//                }
//                is Resource.Error -> {
//                    showLoading(false)
//                    Log.d("Market", "Error ${result.message}")
//                }
//            }
//        }
//    }

//    private fun showOrder(product: List<OrderDtoItem>) {
//        listAdapter.submitList(product)
//        binding.apply {
//            notificationRv.layoutManager = LinearLayoutManager(requireContext())
//            notificationRv.setHasFixedSize(true)
//            notificationRv.adapter = listAdapter
//        }
//    }

    private fun showNotification(notification: List<Notification>) {
        binding.apply {
            notificationRv.layoutManager = LinearLayoutManager(requireContext())
            notificationRv.setHasFixedSize(true)
            notificationRv.adapter = listAdapter
            listAdapter.submitList(notification.sortedByDescending { it.id })
        }
    }

    private fun onClicked(productId: Int) {
    }

    private fun showLoading(value: Boolean) {
        if (value) {
            binding.progressCircular.visibility = View.VISIBLE
        } else {
            binding.progressCircular.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

