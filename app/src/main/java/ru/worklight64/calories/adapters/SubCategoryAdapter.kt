package ru.worklight64.calories.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.worklight64.calories.ProductListActivity
import ru.worklight64.calories.R
import ru.worklight64.calories.databinding.ItemSubCategoryBinding
import ru.worklight64.calories.entities.ItemSubCategoryClass
import ru.worklight64.calories.fragments.FragmentProductCategory


class SubCategoryAdapter(private val context: Context):
    ListAdapter<ItemSubCategoryClass, SubCategoryAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position), context)
    }

    class ItemHolder(view: View): RecyclerView.ViewHolder(view){
        private val itemForm = ItemSubCategoryBinding.bind(view)

        fun setData(item: ItemSubCategoryClass, context: Context) = with(itemForm){
            tvName.text = item.name
            Picasso.get()
                .load("https://calorizator.ru/sites/default/files/imagecache/product_512/product/bombbar-keto-almond-nougat-vanilla.jpg")
                .error(R.drawable.p_brokkoli)
                .into(imageView)

            itemView.setOnClickListener {
                val i = Intent(context, ProductListActivity::class.java).putExtra(
                    FragmentProductCategory.PROD_CAT_KEY, item.slug)
                context.startActivity(i)
            }
        }

        companion object{
            fun create(parent:ViewGroup):ItemHolder{
                return ItemHolder(
                    LayoutInflater.from(parent.context).inflate(
                    R.layout.item_sub_category, parent, false))
            }
        }
    }

    class ItemComparator: DiffUtil.ItemCallback<ItemSubCategoryClass>(){
        override fun areItemsTheSame(oldItem: ItemSubCategoryClass, newItem: ItemSubCategoryClass): Boolean {
            return oldItem.slug == newItem.slug
        }

        override fun areContentsTheSame(oldItem: ItemSubCategoryClass, newItem: ItemSubCategoryClass): Boolean {
            return oldItem == newItem
        }

    }

    interface CategoryListener{
        fun onClickItem(item: ItemSubCategoryClass)
    }
}
