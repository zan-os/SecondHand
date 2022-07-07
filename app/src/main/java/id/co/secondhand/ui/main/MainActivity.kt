package id.co.secondhand.ui.main

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import id.co.secondhand.R
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.databinding.ActivityMainBinding
import id.co.secondhand.utils.Extension.TAG

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
    }

    private fun getAccessToken() {
        viewModel.token.observe(this) { token ->
            getNotification(token)
        }
    }

    private fun getNotification(token: String) {
        viewModel.getNotification(token).observe(this) { result ->
            when (result) {
                is Resource.Loading -> {
                    Log.d(TAG, "getNotification: Loading")
                }
                is Resource.Success -> {
                    Log.d(TAG, "getNotification: ${result.data?.map { it.read }}")
                    result.data?.map { notification ->
                        if (!notification.read) {
                            binding.navView.getOrCreateBadge(R.id.sellerNotificationFragment)
                                .isVisible = true
                        }
                    }
                }
                is Resource.Error -> {
                    Log.d(TAG, "getNotification: ${result.message}")
                }
            }
        }
    }

    private fun setupNavView() {
        val navController = findNavController(R.id.nav_host_fragment)
        binding.navView.setupWithNavController(navController)
    }
}


