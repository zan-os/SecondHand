package id.co.secondhand.ui.market.product.bidderinfo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.co.secondhand.databinding.ActivityBidderInfoBinding

class BidderInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBidderInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBidderInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}