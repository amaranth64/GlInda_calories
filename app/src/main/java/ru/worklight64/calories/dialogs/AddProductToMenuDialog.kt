package ru.worklight64.calories.dialogs

import android.app.AlertDialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
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

        if (item.type != 1) {
            form.linearCalories1por.visibility = View.GONE
            form.tvCaption1por.visibility = View.GONE
        }

        var str = context.getString(R.string.txt_at1por).format(item.weight)
        //str += " (%.1f грамм)".format(product.weight)

        form.tvCaption1por.text = str
        form.tvProtein1por.text = "%.1f".format(item.protein * item.weight/100)
        form.tvCarbo1por.text = "%.1f".format(item.carbo * item.weight/100)
        form.tvFat1por.text = "%.1f".format(item.fat * item.weight/100)
        form.tvEnergy1por.text = "%.1f".format(item.energy * item.weight/100)



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


        form.edPortion.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {


            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val weight = s.toString().toInt()
                val protein = item.protein * weight / 100
                val carbo = item.carbo *  weight / 100
                val fat = item.fat * weight / 100
                val kcal = item.energy * weight / 100

                form.tvProteinPor.text = "%.1f".format(protein)
                form.tvCarboPor.text = "%.1f".format(carbo)
                form.tvFatPor.text = "%.1f".format(fat)
                form.tvEnergyPor.text = "%.1f".format(kcal)
            }
        })

        form.spPortion.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val protein = item.protein * item.weight * spinnerPorItem[position]/ 100
                val carbo = item.carbo * item.weight  * spinnerPorItem[position]/ 100
                val fat = item.fat * item.weight  * spinnerPorItem[position]/ 100
                val kcal = item.energy * item.weight  * spinnerPorItem[position]/ 100

                form.tvProteinPor.text = "%.1f".format(protein)
                form.tvCarboPor.text = "%.1f".format(carbo)
                form.tvFatPor.text = "%.1f".format(fat)
                form.tvEnergyPor.text = "%.1f".format(kcal)

            }
        }

        form.bAdd.setOnClickListener {

            if (form.edPortion.text.isEmpty()) return@setOnClickListener

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