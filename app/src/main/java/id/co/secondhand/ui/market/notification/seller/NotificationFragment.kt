package id.co.secondhand.ui.market.notification.seller

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.co.secondhand.R
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.databinding.FragmentNotificationBinding
import id.co.secondhand.domain.model.notification.Notification
import id.co.secondhand.ui.adapter.NotificationListAdapter
import id.co.secondhand.ui.auth.login.LoginActivity
import id.co.secondhand.ui.main.MainActivity
import id.co.secondhand.utils.Extension.showSnackbar

@AndroidEntryPoint
class NotificationFragment : Fragment(R.layout.fragment_notification) {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    private lateinit var token: String
    private val viewModel: NotificationViewModel by viewModels()
    private val listAdapter: NotificationListAdapter by lazy { NotificationListAdapter(::onClicked) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNotificationBinding.bind(view)
        getAccessToken()
    }

    private fun getAccessToken() {
        viewModel.token.observe(viewLifecycleOwner) { token ->
            credentialCheck(token)
        }
    }

    private fun credentialCheck(token: String) {
        if (token.isEmpty()) {
            binding.notLoggedInLayout.root.visibility = View.VISIBLE
            binding.container.visibility = View.GONE
            navigateToLogin()
        } else {
            binding.notLoggedInLayout.root.visibility = View.GONE
            binding.container.visibility = View.VISIBLE
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
        readNotification(token, notification.id)
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

    private fun navigateToLogin() {
        binding.notLoggedInLayout.loginBtn.setOnClickListener {
            val direction = Intent(requireContext(), LoginActivity::class.java)
            startActivity(direction)
            requireActivity()
        }
    }

    override fun onDestroyView() {
        super.onDestroy()
        _binding = null
    }
}

