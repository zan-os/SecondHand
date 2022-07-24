package id.co.secondhand.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.co.secondhand.R
import id.co.secondhand.data.remote.response.OrderDtoItem
import id.co.secondhand.databinding.ProductItemListBinding
import id.co.secondhand.utils.Extension.currencyFormatter
import id.co.secondhand.utils.Extension.dateTimeFormatter

class OrderListAdapter(private val onClick: (Int) -> Unit) :
    ListAdapter<OrderDtoItem, OrderListAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(private val binding: ProductItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: OrderDtoItem) {
            binding.apply {
                productNameTv.text = order.product?.name
                productPriceTv.text = order.product?.basePrice?.currencyFormatter()
                bargainPriceTv.text = itemView.resources.getString(
                    R.string.data_ditawar,
                    order.price?.currencyFormatter()
                )
                dateTimeTv.text = order.createdAt?.dateTimeFormatter()
                Glide.with(itemView)
                    .load(order.product?.imageUrl)
                    .placeholder(R.drawable.ic_error_image)
                    .dontAnimate()
                    .dontTransform()
                    .into(binding.productImageIv)
                root.setOnClickListener { onClick(order.id ?: 0) }
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

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<OrderDtoItem>() {
            override fun areItemsTheSame(oldItem: OrderDtoItem, newItem: OrderDtoItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: OrderDtoItem, newItem: OrderDtoItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}