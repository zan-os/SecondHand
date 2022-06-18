package id.co.secondhand.ui.market.product.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.co.secondhand.databinding.FragmentNegotiateBinding

class NegotiateFragment: BottomSheetDialogFragment() {

    private var _bindig: FragmentNegotiateBinding? = null
    private val binding get() = _bindig!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bindig = FragmentNegotiateBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bindig = null
    }
}