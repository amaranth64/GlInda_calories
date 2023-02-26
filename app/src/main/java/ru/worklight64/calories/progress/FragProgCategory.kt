package ru.worklight64.calories.progress

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ru.worklight64.calories.ProductListActivity
import ru.worklight64.calories.R
import ru.worklight64.calories.adapters.CategoryAdapter
import ru.worklight64.calories.databinding.FragProgCategoryBinding
import ru.worklight64.calories.fragments.FragmentProductCategory
import ru.worklight64.calories.utils.CommonConst
import ru.worklight64.calories.utils.JsonHelper

private const val ARG_PARAM1 = "param1"

class FragProgCategory : Fragment(), ItemClickListener {
    private lateinit var form: FragProgCategoryBinding
    private lateinit var adapter: CategoryAdapter
    private lateinit var pref: SharedPreferences
    private var param1: ProgressInteractor? = null

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
        form = FragProgCategoryBinding.inflate(inflater, container, false)
        return form.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
    }

    private fun initRcView(){
        pref = PreferenceManager.getDefaultSharedPreferences(requireActivity())
        val linear = getString(R.string.pref_linear)
        if (pref.getString(CommonConst.KEY_LINEAR, linear) == linear) form.rcView.layoutManager = LinearLayoutManager(activity)
        else form.rcView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        val itemList = JsonHelper.getCategoryList("jsonCategoryProduct.json", requireContext())

        adapter = CategoryAdapter(requireContext(), pref,  this)
        adapter.submitList(itemList)

        if (itemList.isEmpty()) form.tvEmpty.visibility = View.VISIBLE else form.tvEmpty.visibility = View.GONE
        form.rcView.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance(s: ProgressInteractor) = FragProgCategory().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_PARAM1, s)
            }
        }
    }

    override fun itemClick(a: Any) {
        val slug = a as String
        param1?.category_slug = slug
        param1?.setStep("product")
    }
}