package ru.worklight64.calories.adapters

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.worklight64.calories.R
import ru.worklight64.calories.databinding.ItemMenuNameBinding
import ru.worklight64.calories.entities.MenuNameListItem

class MyFoodAdapter(private var listener: MyFoodListener, private val defPref: SharedPreferences):
    ListAdapter<MenuNameListItem, MyFoodAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position), listener, defPref)
    }

    class ItemHolder(view: View): RecyclerView.ViewHolder(view){
        private val itemForm = ItemMenuNameBinding.bind(view)
        //private val itemForm = TaskItemBinding.bind(view)

        fun setData(item: MenuNameListItem, listener: MyFoodListener, defPref: SharedPreferences)= with(itemForm){
            itemForm.tvName.text = item.name

            itemForm.tvProtein.text = "%.1f".format(item.protein)
            itemForm.tvCarbo.text = "%.1f".format(item.carbo)
            itemForm.tvFat.text = "%.1f".format(item.fat)
            itemForm.tvEnergy.text = "%.1f".format(item.energy)

            itemView.setOnClickListener {
                listener.onClickItem(item)
            }

//            itemForm.ibEdit.setOnClickListener {
//                listener.editItem(item)
//            }
//            itemForm.ibDelete.setOnClickListener {
//                listener.deleteItem(item)
//            }

        }

        companion object{
            fun create(parent:ViewGroup):ItemHolder{
                return ItemHolder(
                    LayoutInflater.from(parent.context).inflate(
                    R.layout.item_menu_name, parent, false))
            }
        }
    }

    class ItemComparator: DiffUtil.ItemCallback<MenuNameListItem>(){
        override fun areItemsTheSame(oldItem: MenuNameListItem, newItem: MenuNameListItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MenuNameListItem, newItem: MenuNameListItem): Boolean {
            return oldItem == newItem
        }

    }

    interface MyFoodListener{
        fun onClickItem(item: MenuNameListItem)
        fun deleteItem(item: MenuNameListItem)
        fun editItem(item: MenuNameListItem)
    }
}
