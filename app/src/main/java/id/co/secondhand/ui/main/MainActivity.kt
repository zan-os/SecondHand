package id.co.secondhand.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import id.co.secondhand.R
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.databinding.ActivityMainBinding

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
                is Resource.Loading -> {}
                is Resource.Success -> {
                    result.data?.map { notification ->
                        binding.navView.getOrCreateBadge(R.id.sellerNotificationFragment)
                            .isVisible = !notification.read
                    }
                }
                is Resource.Error -> {}
            }
        }
    }

    private fun setupNavView() {
        val navController = findNavController(R.id.nav_host_fragment)
        binding.navView.setupWithNavController(navController)
    }
}


