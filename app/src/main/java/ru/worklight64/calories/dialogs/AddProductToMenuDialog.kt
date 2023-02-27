package ru.worklight64.calories.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import com.squareup.picasso.Picasso
import ru.worklight64.calories.R
import ru.worklight64.calories.databinding.DialogAddProductToMenuBinding
import ru.worklight64.calories.entities.ItemProductClass
import ru.worklight64.calories.entities.MenuNameListItem
import ru.worklight64.calories.utils.CommonConst

object AddProductToMenuDialog {

    fun showDialog(context: Context, listener: Listener, item: ItemProductClass, menu: List<MenuNameListItem>){

        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        val form = DialogAddProductToMenuBinding.inflate(LayoutInflater.from(context))
        builder.setView(form.root)

        form.tvName.text = item.title
        form.tvDecs.text = item.description

        Picasso.get()
            .load(item.url_picture)
            .error(R.drawable.p_brokkoli)
            .into(form.ivProduct)

        form.tvProtein100.text = "%.1f".format(item.protein)
        form.tvCarbo100.text = "%.1f".format(item.carbo)
        form.tvFat100.text = "%.1f".format(item.fat)
        form.tvEnergy100.text = "%.1f".format(item.energy)

        if (item.type == CommonConst.TYPE_WEIGHT) form.spPortion.visibility = View.GONE
        if (item.type == CommonConst.TYPE_100) form.edPortion.visibility = View.GONE

        val spinnerPorItem = arrayListOf<Int>(1,2,3,4,5)
        val adapterPor = ArrayAdapter(context, android.R.layout.simple_spinner_item, spinnerPorItem)
        adapterPor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        form.spPortion.adapter = adapterPor


        val spinnerMenuItem = ArrayList<String>()
        menu.forEach{spinnerMenuItem.add(it.name)}
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, spinnerMenuItem)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        form.spMenuName.adapter = adapter


        form.bAdd.setOnClickListener {
            var weight = 0.0
            var count = 0
            if (item.type == CommonConst.TYPE_WEIGHT) {
                weight = form.edPortion.text.toString().toDouble()
            }
            if (item.type == CommonConst.TYPE_100) {
                weight = item.weight * form.spPortion.selectedItem.toString().toInt()
                count = form.spPortion.selectedItem.toString().toInt()
            }
            val menuID = menu[form.spMenuName.selectedItemId.toInt()].id
            if (menuID != null) {
                listener.onAdd(item.slug, weight, count, menuID)
            }
            dialog?.dismiss()
        }

        form.bCancel.setOnClickListener {
            dialog?.dismiss()
        }

        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(null)

        dialog.show()
    }

    interface Listener{
        fun onAdd(slug:String, weight: Double, count: Int, menuID: Int)
    }

}