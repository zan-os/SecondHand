package id.co.secondhand.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import id.co.secondhand.R
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.databinding.ActivityMainBinding
import id.co.secondhand.domain.model.notification.Notification

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition { viewModel.isLoading.value }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getAccessToken()
        setupNavView()
        notificationClick()
    }

    fun getAccessToken() {
        viewModel.token.observe(this) { token ->
            getNotification(token)
        }
    }

    private fun getNotification(token: String) {
        viewModel.getNotification(token).observe(this) { result ->
            when (result) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    result.data?.map { notificationStatus(it) }
                }
                is Resource.Error -> {}
            }
        }
    }

    private fun notificationStatus(notification: Notification) {
        if (!notification.read) {
            binding.navView.getOrCreateBadge(R.id.notificationFragment).isVisible = true
        }
    }

    private fun notificationClick() {
        binding.navView.menu.findItem(R.id.notificationFragment).setOnMenuItemClickListener {
            binding.navView.getOrCreateBadge(R.id.notificationFragment).isVisible = false
            return@setOnMenuItemClickListener false
        }
    }

    private fun setupNavView() {
        val navController = findNavController(R.id.nav_host_fragment)
        binding.navView.setupWithNavController(navController)
    }
}


