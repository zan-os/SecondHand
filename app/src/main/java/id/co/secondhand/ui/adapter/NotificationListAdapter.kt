package id.co.secondhand.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.co.secondhand.R
import id.co.secondhand.databinding.ProductItemListBinding
import id.co.secondhand.domain.model.notification.Notification
import id.co.secondhand.utils.Extension.currencyFormatter
import id.co.secondhand.utils.Extension.dateTimeFormatter

class NotificationListAdapter(private val onClick: (Int) -> Unit) :
    ListAdapter<Notification, NotificationListAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(private val binding: ProductItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(notification: Notification) {
            binding.apply {
                if (notification.status == "create") {
                    statusTv.text = "Berhasil diterbitkan"
                    bargainPriceTv.visibility = View.GONE
                }
                productNameTv.text = notification.product.name
                productPriceTv.text = notification.product.basePrice.currencyFormatter()
                bargainPriceTv.text =
                    itemView.resources.getString(
                        R.string.data_ditawar,
                        notification.bidPrice.currencyFormatter()
                    )
                dateTimeTv.text = notification.createdAt.dateTimeFormatter()
                Glide.with(itemView)
                    .load(notification.product.imageUrl)
                    .placeholder(R.drawable.ic_error_image)
                    .dontAnimate()
                    .dontTransform()
                    .into(binding.productImageIv)
                root.setOnClickListener { onClick(notification.id) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ProductItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Notification>() {
            override fun areItemsTheSame(
                oldItem: Notification,
                newItem: Notification
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Notification,
                newItem: Notification
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}