package id.co.secondhand.ui.market.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.databinding.FragmentProfileBinding
import id.co.secondhand.domain.model.auth.User
import id.co.secondhand.ui.auth.login.LoginActivity
import id.co.secondhand.ui.market.profile.update.EditProfileActivity
import id.co.secondhand.utils.Extension.EXTRA_USER

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAccessToken()
        logout()
    }

    private fun getAccessToken() {
        viewModel.token.observe(viewLifecycleOwner) { token ->
            getUserData(token)
        }
    }

    private fun getUserData(accessToken: String) {
        viewModel.getUserData(accessToken).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    Log.d("Market", "Loading")
                }
                is Resource.Success -> {
                    Log.d("Market", result.data.toString())
                    result.data?.let {
                        showUserData(it)
                        updateProfile(it)
                    }
                }
                is Resource.Error -> {
                    Log.d("Market", "Error ${result.message.toString()}")
                }
            }
        }
    }

    private fun showUserData(user: User) {
        binding.apply {
            Glide.with(requireContext())
                .load(user.imageUrl)
                .override(300)
                .centerCrop()
                .into(photoProfileIv)
            fullNameTv.text = user.fullName

            editProfileTv.setOnClickListener {
                val editProfileIntent = Intent(requireContext(), EditProfileActivity::class.java)
                editProfileIntent.putExtra(EXTRA_USER, user)
                startActivity(editProfileIntent)
            }
        }
    }

    private fun updateProfile(user: User) {
        binding.editProfileTv.setOnClickListener {
            val direction = Intent(requireContext(), EditProfileActivity::class.java)
            direction.putExtra(EXTRA_USER, user)
            startActivity(direction)
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