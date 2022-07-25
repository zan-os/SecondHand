package id.co.secondhand.ui.market.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import id.co.secondhand.R
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.databinding.FragmentProfileBinding
import id.co.secondhand.domain.model.auth.User
import id.co.secondhand.ui.auth.login.LoginActivity
import id.co.secondhand.ui.market.profile.update.EditProfileActivity
import id.co.secondhand.utils.Constants.EXTRA_USER

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)
        getAccessToken()
        logout()
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
            getUserData(token)
        }
    }

    private fun getUserData(accessToken: String) {
        viewModel.getUserData(accessToken).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Success -> {
                    showLoading(false)
                    result.data?.let {
                        showUserData(it)
                        navigateToEditProfile(it)
                    }
                }
                is Resource.Error -> {
                    showLoading(false)
                }
            }
        }
    }

    private fun showUserData(user: User) {
        binding.apply {
            container.visibility = View.VISIBLE
            if (user.imageUrl != null) {
                Glide.with(requireContext())
                    .load(user.imageUrl)
                    .override(300)
                    .centerCrop()
                    .into(photoProfileIv)
            }
            fullNameTv.text = user.fullName

            editProfileTv.setOnClickListener {
                val editProfileIntent = Intent(requireContext(), EditProfileActivity::class.java)
                editProfileIntent.putExtra(EXTRA_USER, user)
                startActivity(editProfileIntent)
            }
        }
    }

    private fun showLoading(visible: Boolean) {
        binding.progressCircular.isVisible = visible
    }

    private fun navigateToEditProfile(user: User) {
        binding.editProfileTv.setOnClickListener {
            val direction = Intent(requireContext(), EditProfileActivity::class.java)
            direction.putExtra(EXTRA_USER, user)
            startActivity(direction)
        }
    }

    private fun navigateToLogin() {
        binding.notLoggedInLayout.loginBtn.setOnClickListener {
            val direction = Intent(requireContext(), LoginActivity::class.java)
            startActivity(direction)
            requireActivity()
        }
    }

    private fun logout() {
        binding.logOutTv.setOnClickListener {
            viewModel.clearCredential()
            val direction = Intent(requireContext(), LoginActivity::class.java)
            startActivity(direction)
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}