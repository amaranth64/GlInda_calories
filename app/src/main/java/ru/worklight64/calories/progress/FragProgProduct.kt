package ru.worklight64.calories.progress

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ru.worklight64.calories.R
import ru.worklight64.calories.adapters.ProductListAdapter
import ru.worklight64.calories.databinding.FragProgProductBinding
import ru.worklight64.calories.entities.ItemProductClass
import ru.worklight64.calories.fragments.FragmentProductCategory
import ru.worklight64.calories.utils.CommonConst
import ru.worklight64.calories.utils.DataContainerHelper

private const val ARG_PARAM1 = "param1"

class FragProgProduct : Fragment(), ProductListAdapter.ProductListListener {
    private lateinit var form: FragProgProductBinding
    private lateinit var adapter: ProductListAdapter
    private lateinit var pref: SharedPreferences
    private var param1: ProgressInteractor? = null

    var category_slug = ""

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
        form = FragProgProductBinding.inflate(inflater, container, false)
        return form.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        category_slug = param1?.category_slug.toString()
        if (!category_slug.isNullOrEmpty()) {
            initSharedPreferences()
            initRecyclerView()
        } else {
            param1?.setStep("category")
        }

    }

    private fun initSharedPreferences(){
        pref = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val linear = getString(R.string.pref_linear)
        if (pref.getString(CommonConst.KEY_LINEAR,linear) == linear) form.rcView.layoutManager = LinearLayoutManager(requireContext())
        else form.rcView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    private fun initRecyclerView(){

        val itemList = DataContainerHelper.getContainer(requireContext(), category_slug)

        adapter = ProductListAdapter(this@FragProgProduct, pref)
        adapter.submitList(itemList)
        if (itemList.isEmpty()) form.tvEmpty.visibility = View.VISIBLE else form.tvEmpty.visibility = View.GONE
        form.rcView.adapter = adapter
    }


    companion object {
        @JvmStatic
        fun newInstance(s: ProgressInteractor) =
            FragProgProduct().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, s)
                }
            }
    }


    override fun onClickItem(item: ItemProductClass) {

        param1?.product_item = item
        param1?.setStep("weight")
    }

    override fun addItem(item: ItemProductClass) {

    }
}