package ru.worklight64.calories.adapters

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.worklight64.calories.entities.ItemCategoryClass
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.worklight64.calories.R
import ru.worklight64.calories.databinding.ItemCategoryBinding

class CategoryAdapter(private var listener: CategoryListener, private val defPref: SharedPreferences):
    ListAdapter<ItemCategoryClass, CategoryAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position), listener, defPref)
    }

    class ItemHolder(view: View): RecyclerView.ViewHolder(view){
        private val itemForm = ItemCategoryBinding.bind(view)

        fun setData(item: ItemCategoryClass, listener: CategoryListener, defPref: SharedPreferences)= with(itemForm){
            tvName.text = item.name

            itemView.setOnClickListener {
                listener.onClickItem(item)
            }
        }

        companion object{
            fun create(parent:ViewGroup):ItemHolder{
                return ItemHolder(
                    LayoutInflater.from(parent.context).inflate(
                    R.layout.item_category, parent, false))
            }
        }
    }

    class ItemComparator: DiffUtil.ItemCallback<ItemCategoryClass>(){
        override fun areItemsTheSame(oldItem: ItemCategoryClass, newItem: ItemCategoryClass): Boolean {
            return oldItem.slug == newItem.slug
        }

        override fun areContentsTheSame(oldItem: ItemCategoryClass, newItem: ItemCategoryClass): Boolean {
            return oldItem == newItem
        }

    }

    interface CategoryListener{
        fun onClickItem(item: ItemCategoryClass)
    }
}
