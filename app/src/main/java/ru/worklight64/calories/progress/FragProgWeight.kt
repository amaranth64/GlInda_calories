package ru.worklight64.calories.progress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
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

        if (product.type == CommonConst.TYPE_WEIGHT) form.spPortion.visibility = View.GONE
        if (product.type == CommonConst.TYPE_100) form.edPortion.visibility = View.GONE

        val spinnerPorItem = arrayListOf<Int>(1,2,3,4,5)
        val adapterPor = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerPorItem)
        adapterPor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        form.spPortion.adapter = adapterPor

        form.bAdd.setOnClickListener {

            if (product.type == CommonConst.TYPE_WEIGHT) {
                param1?.product_weight = form.edPortion.text.toString().toDouble()
                param1?.product_count = 0
            }
            if (product.type == CommonConst.TYPE_100) {
                param1?.product_weight = product.weight * form.spPortion.selectedItem.toString().toDouble()
                param1?.product_count = form.spPortion.selectedItem.toString().toInt()
            }

            param1?.setStep(ProgSteps.FINAL)
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