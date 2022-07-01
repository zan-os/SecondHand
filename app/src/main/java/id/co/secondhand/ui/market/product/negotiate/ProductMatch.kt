package id.co.secondhand.ui.market.product.negotiate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import id.co.secondhand.R
import id.co.secondhand.databinding.FragmentProductMatchBinding

@AndroidEntryPoint
class ProductMatch : Fragment() {

    private var _binding: FragmentProductMatchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductMatchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_match, container, false)
    }

}