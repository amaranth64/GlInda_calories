package ru.worklight64.calories.fragments

import android.content.Context
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
import ru.worklight64.calories.adapters.CategoryAdapter
import ru.worklight64.calories.databinding.FragmentProductBinding
import ru.worklight64.calories.entities.ItemCategoryClass
import ru.worklight64.calories.utils.JsonHelper


class FragmentMyFood : Fragment(), CategoryAdapter.CategoryListener {
    private lateinit var form: FragmentProductBinding
    private lateinit var pref: SharedPreferences
    private lateinit var adapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        form = FragmentProductBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return form.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()

    }

    private fun initRcView(){
        pref = PreferenceManager.getDefaultSharedPreferences(requireActivity())
        val linear = getString(R.string.pref_linear)
        if (pref.getString("product_style_key",linear) == linear) form.rcViewProduct.layoutManager = LinearLayoutManager(activity)
        else form.rcViewProduct.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        val itemList = JsonHelper.getList("jsonCategoryMyFood.json", requireContext())
        adapter = CategoryAdapter(this@FragmentMyFood, pref)
        adapter.submitList(itemList)
        if (itemList.isEmpty()) form.tvEmpty.visibility = View.VISIBLE else form.tvEmpty.visibility = View.GONE
        form.rcViewProduct.adapter = adapter
    }

    companion object {

        @JvmStatic
        fun newInstance() = FragmentMyFood()
    }



    override fun onClickItem(item: ItemCategoryClass) {

    }
}