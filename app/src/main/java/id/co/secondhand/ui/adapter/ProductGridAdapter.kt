package id.co.secondhand.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.co.secondhand.databinding.ProductItemGridBinding
import id.co.secondhand.domain.model.Product

class ProductGridAdapter : ListAdapter<Product, ProductGridAdapter.ViewHolder>(DIFF_CALLBACK) {

    class ViewHolder(private val binding: ProductItemGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                productNameTv.text = product.name
                productPriceTv.text = product.basePrice.toString()
                product.categories?.map { binding.productCategoryTv.text = it.name }
                Glide.with(itemView)
                    .load(product.imageUrl)
                    .into(binding.productImageIv)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ProductItemGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }
        }
    }
}