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
import id.co.secondhand.R
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.databinding.FragmentSellerNotificationBinding
import id.co.secondhand.domain.model.notification.Notification
import id.co.secondhand.ui.adapter.NotificationListAdapter
import id.co.secondhand.utils.Extension.TAG
import id.co.secondhand.utils.Extension.showSnackbar

@AndroidEntryPoint
class SellerNotificationFragment : Fragment() {

    private var _binding: FragmentSellerNotificationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SellerNotificationViewModel by viewModels()

    private lateinit var accessToken: String
    private val listAdapter: NotificationListAdapter by lazy { NotificationListAdapter(::onClicked) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSellerNotificationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getAccessToken()
    }

    private fun getAccessToken() {
        viewModel.token.observe(viewLifecycleOwner) { token ->
            getNotification(token)
            accessToken = token
        }
    }

    private fun getNotification(accessToken: String) {
        viewModel.getNotification(accessToken).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    showLoading(true)
                    Log.d(TAG, "Loading")
                }
                is Resource.Success -> {
                    showLoading(false)
                    showNotification(result.data ?: emptyList())
                    Log.d(TAG, result.data.toString())
                }
                is Resource.Error -> {
                    showLoading(false)
                    result.message?.showSnackbar(
                        binding.root,
                        requireContext(),
                        R.color.white,
                        R.color.alert_danger
                    )
                    Log.d(TAG, "Error ${result.message}")
                }
            }
        }
    }

    private fun showNotification(notification: List<Notification>) {
        binding.apply {
            notificationRv.layoutManager = LinearLayoutManager(requireContext())
            notificationRv.setHasFixedSize(true)
            notificationRv.adapter = listAdapter
            listAdapter.submitList(notification.sortedByDescending { it.id })
        }
    }

    private fun readNotification(accessToken: String, id: Int) {
        viewModel.readNotification(accessToken, id).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    Log.d(TAG, "readNotification: Loading")
                }
                is Resource.Success -> {
                    Log.d(TAG, "readNotification: Success")
                }
                is Resource.Error -> {
                    Log.d(TAG, "readNotification: ${result.message}")
                }
            }
        }
    }

    private fun onClicked(notification: Notification) {
        readNotification(accessToken, notification.id)
        when (notification.status) {
            "create" -> {
            }
            "bid" -> {
            }
            else -> {
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

