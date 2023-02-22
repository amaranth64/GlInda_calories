package ru.worklight64.calories.adapters

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.worklight64.calories.R

import ru.worklight64.calories.databinding.ItemProductBinding
import ru.worklight64.calories.entities.ItemProductClass

class ProductInMenuAdapter(private var listener: ProductInMenuListener, private val defPref: SharedPreferences):
    ListAdapter<ItemProductClass, ProductInMenuAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position), listener, defPref)
    }

    class ItemHolder(view: View): RecyclerView.ViewHolder(view){
        private val itemForm = ItemProductBinding.bind(view)

        fun setData(item: ItemProductClass, listener: ProductInMenuListener, defPref: SharedPreferences)= with(itemForm){
            itemForm.tvName.text = item.title
            if (item.description.isNotEmpty()) itemForm.tvDecs.text = item.description else itemForm.tvDecs.visibility = View.GONE
            itemForm.tvProtein.text = "%.1f".format(item.protein)
            itemForm.tvCarbo.text = "%.1f".format(item.carbo)
            itemForm.tvFat.text = "%.1f".format(item.fat)
            itemForm.tvEnergy.text = "%.1f".format(item.energy)

            Picasso.get()
                .load(item.url_picture)
                .error(R.drawable.p_brokkoli)
                .into(itemForm.ivProduct)


            itemView.setOnClickListener {
                listener.onClickItem(item)
            }


        }

        companion object{
            fun create(parent:ViewGroup):ItemHolder{
                return ItemHolder(
                    LayoutInflater.from(parent.context).inflate(
                    R.layout.item_product, parent, false))
            }
        }
    }

    class ItemComparator: DiffUtil.ItemCallback<ItemProductClass>(){
        override fun areItemsTheSame(oldItem: ItemProductClass, newItem: ItemProductClass): Boolean {
            return oldItem.url_picture == newItem.url_picture
        }

        override fun areContentsTheSame(oldItem: ItemProductClass, newItem: ItemProductClass): Boolean {
            return oldItem == newItem
        }

    }

    interface ProductInMenuListener{
        fun onClickItem(item: ItemProductClass)
        fun deleteItem(item: ItemProductClass)
    }
}
