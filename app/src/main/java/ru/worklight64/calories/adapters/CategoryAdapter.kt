package ru.worklight64.calories.adapters

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.worklight64.calories.R
import ru.worklight64.calories.databinding.ItemCategoryBinding
import ru.worklight64.calories.entities.ItemCategoryClass
import ru.worklight64.calories.progress.ItemClickListener
import ru.worklight64.calories.utils.JsonHelper


class CategoryAdapter(private val context: Context,  private val defPref: SharedPreferences, private val listener: ItemClickListener):
    ListAdapter<ItemCategoryClass, CategoryAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position), defPref, context, listener)
    }

    class ItemHolder(view: View): RecyclerView.ViewHolder(view){
        private val itemForm = ItemCategoryBinding.bind(view)

        fun setData(item: ItemCategoryClass, defPref: SharedPreferences, context: Context, listener: ItemClickListener)= with(itemForm){

            if (item.subcategory.isNotEmpty()){
                val adapter = SubCategoryAdapter(context, listener)
                listSubcat.adapter = adapter
                listSubcat.layoutManager = LinearLayoutManager(context)
                val data = JsonHelper.getSubCategoryList(item.subcategory + ".json", context)
                adapter.submitList(data)


                if (data.isNotEmpty()) ivExpand.visibility = View.VISIBLE else ivExpand.visibility = View.INVISIBLE
            } else
                ivExpand.visibility = View.INVISIBLE


            tvName.text = item.name
            Picasso.get()
                .load("https://calorizator.ru/sites/default/files/imagecache/product_512/product/bombbar-keto-almond-nougat-vanilla.jpg")
                .error(R.drawable.p_brokkoli)
                .into(imageView)

            itemView.setOnClickListener {
                if (item.expanded) {
                    listSubcat.visibility = View.GONE
                    item.expanded = false
                } else {

                    if (item.subcategory.isEmpty()){

                    } else {
                        listSubcat.visibility = View.VISIBLE
                        item.expanded = true
                    }

                }
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



}
