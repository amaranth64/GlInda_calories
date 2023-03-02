package ru.worklight64.calories.progress

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import ru.worklight64.calories.R
import ru.worklight64.calories.databinding.FragProgWeightBinding
import ru.worklight64.calories.entities.ItemProductClass
import ru.worklight64.calories.utils.CommonConst

private const val ARG_PARAM1 = "param1"

class FragProgWeight : Fragment() {
    private lateinit var form: FragProgWeightBinding
    private var param1: ProgressInteractor? = null

    private lateinit var product: ItemProductClass
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getSerializable(ARG_PARAM1) as ProgressInteractor?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        form = FragProgWeightBinding.inflate(inflater, container, false)
        return form.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (param1?.product_item!!.title == "") {
            param1?.setStep(ProgSteps.PRODUCT)
            return
        }
        product = param1?.product_item!!
        init()

    }

    private fun init(){

        form.tvName.text = product.title
        form.tvDecs.text = product.description

        Picasso.get()
            .load(product.url_picture)
            .error(ru.worklight64.calories.R.drawable.p_brokkoli)
            .into(form.ivProduct)

        form.tvProtein100.text = "%.1f".format(product.protein)
        form.tvCarbo100.text = "%.1f".format(product.carbo)
        form.tvFat100.text = "%.1f".format(product.fat)
        form.tvEnergy100.text = "%.1f".format(product.energy)

        if (product.type != 1) {
            form.linearCalories1por.visibility = View.GONE
            form.tvCaption1por.visibility = View.GONE
        }

        var str = resources.getString(R.string.txt_at1por).format(product.weight)
        //str += " (%.1f грамм)".format(product.weight)

        form.tvCaption1por.text = str
        form.tvProtein1por.text = "%.1f".format(product.protein * product.weight/100)
        form.tvCarbo1por.text = "%.1f".format(product.carbo * product.weight/100)
        form.tvFat1por.text = "%.1f".format(product.fat * product.weight/100)
        form.tvEnergy1por.text = "%.1f".format(product.energy * product.weight/100)

        if (product.type == CommonConst.TYPE_WEIGHT) form.spPortion.visibility = View.GONE
        if (product.type == CommonConst.TYPE_100) form.edPortion.visibility = View.GONE

        val spinnerPorItem = arrayListOf<Int>(1,2,3,4,5)
        val adapterPor = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerPorItem)
        adapterPor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        form.spPortion.adapter = adapterPor

        form.bAdd.setOnClickListener {

            if (product.type == CommonConst.TYPE_WEIGHT) {
                if (form.edPortion.text.isEmpty()) return@setOnClickListener

                param1?.product_weight = form.edPortion.text.toString().toDouble()
                param1?.product_count = 0
            }
            if (product.type == CommonConst.TYPE_100) {
                param1?.product_weight = product.weight * form.spPortion.selectedItem.toString().toDouble()
                param1?.product_count = form.spPortion.selectedItem.toString().toInt()
            }

            param1?.setStep(ProgSteps.FINAL)
        }

        form.edPortion.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val weight = s.toString().toInt()
                val protein = product.protein * weight / 100
                val carbo = product.carbo *  weight / 100
                val fat = product.fat * weight / 100
                val kcal = product.energy * weight / 100

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
                val protein = product.protein * product.weight * spinnerPorItem[position]/ 100
                val carbo = product.carbo * product.weight  * spinnerPorItem[position]/ 100
                val fat = product.fat * product.weight  * spinnerPorItem[position]/ 100
                val kcal = product.energy * product.weight  * spinnerPorItem[position]/ 100

                form.tvProteinPor.text = "%.1f".format(protein)
                form.tvCarboPor.text = "%.1f".format(carbo)
                form.tvFatPor.text = "%.1f".format(fat)
                form.tvEnergyPor.text = "%.1f".format(kcal)

            }
        }

    }
    companion object {
        @JvmStatic
        fun newInstance(s: ProgressInteractor) = FragProgWeight().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_PARAM1, s)
            }
        }
    }
}