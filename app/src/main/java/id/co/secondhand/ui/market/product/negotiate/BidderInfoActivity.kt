package id.co.secondhand.ui.market.product.negotiate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.co.secondhand.R
import id.co.secondhand.databinding.ActivityBidderInfoBinding

class BidderInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBidderInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bidder_info)
        setContentView(binding.root)
    }

    val bundle = intent.extras
    val productId = bundle?.getInt("id")

}