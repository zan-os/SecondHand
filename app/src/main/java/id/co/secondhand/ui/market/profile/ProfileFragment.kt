package id.co.secondhand.ui.market.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import id.co.secondhand.R
import id.co.secondhand.databinding.FragmentProfileBinding
import id.co.secondhand.ui.auth.login.LoginActivity

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateProfile()
        logout()
    }

    private fun updateProfile() {
        binding.editProfileTv.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_updateProfileFragment)
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