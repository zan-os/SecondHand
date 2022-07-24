package id.co.secondhand.ui.market.notification.seller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.co.secondhand.R
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.databinding.FragmentSellerNotificationBinding
import id.co.secondhand.domain.model.notification.Notification
import id.co.secondhand.ui.adapter.NotificationListAdapter
import id.co.secondhand.ui.main.MainActivity
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAccessToken()
    }

    private fun getAccessToken() {
        viewModel.token.observe(viewLifecycleOwner) { token ->
            accessToken = token
            getNotification(token)
        }
    }

    private fun getNotification(accessToken: String) {
        viewModel.getNotification(accessToken).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Success -> {
                    showLoading(false)
                    showNotification(result.data ?: emptyList())
                }
                is Resource.Error -> {
                    showLoading(false)
                    result.message?.let { showErrorMessage(it) }
                }
            }
        }
    }

    private fun readNotification(accessToken: String, id: Int) {
        viewModel.readNotification(accessToken, id).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    getNotification(accessToken)
                    MainActivity().getAccessToken()
                }
                is Resource.Error -> {
                    result.message?.let { showErrorMessage(it) }
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

    private fun onClicked(notification: Notification) {
        readNotification(accessToken, notification.id)
        when (notification.status) {
            "create" -> {}
            "bid" -> {}
            else -> {}
        }
    }

    private fun showLoading(visible: Boolean) {
        binding.progressCircular.isVisible = visible
    }

    private fun showErrorMessage(message: String) {
        message.showSnackbar(
            view = requireView(),
            context = requireContext(),
            textColor = R.color.white,
            backgroundColor = R.color.alert_danger
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

