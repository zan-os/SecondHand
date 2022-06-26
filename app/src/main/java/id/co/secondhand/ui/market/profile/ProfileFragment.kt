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
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dagger.hilt.android.AndroidEntryPoint
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.databinding.FragmentProfileBinding
import id.co.secondhand.domain.model.auth.User
import id.co.secondhand.ui.auth.login.LoginActivity
import id.co.secondhand.ui.market.profile.update.EditProfileActivity

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
        updateProfile()
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
                    result.data?.let { showUserData(it) }
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
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(photoProfileIv)
            fullNameTv.text = user.fullName
        }
    }

    private fun updateProfile() {
        binding.editProfileTv.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
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