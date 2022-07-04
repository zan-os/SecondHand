package id.co.secondhand.ui.market.product.bidderinfo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.co.secondhand.databinding.ActivityBidderInfoBinding
import id.co.secondhand.domain.model.notification.Notification
import id.co.secondhand.utils.Extension.EXTRA_NOTIFICATION

class BidderInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBidderInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBidderInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        arrowBack()
        fetchData()
    }

    private fun fetchData() {
        binding.apply {
            val data = intent.getParcelableExtra<Notification>(EXTRA_NOTIFICATION) as Notification
            buyerNameTv.text = data.buyerName
        }
    }

    private fun arrowBack() {
        binding.materialToolbar.setNavigationOnClickListener { finish() }
    }
}