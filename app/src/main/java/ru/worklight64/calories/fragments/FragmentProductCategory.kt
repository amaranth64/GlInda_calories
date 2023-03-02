package ru.worklight64.calories.fragments


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ru.worklight64.calories.MainApp
import ru.worklight64.calories.ProductListActivity
import ru.worklight64.calories.R
import ru.worklight64.calories.adapters.CategoryAdapter
import ru.worklight64.calories.databinding.FragmentProductBinding
import ru.worklight64.calories.db.MainViewModel
import ru.worklight64.calories.entities.MenuNameListItem
import ru.worklight64.calories.progress.ItemClickListener
import ru.worklight64.calories.utils.CommonConst
import ru.worklight64.calories.utils.JsonHelper


class FragmentProductCategory : Fragment(), ItemClickListener {
    private lateinit var form: FragmentProductBinding
    private lateinit var pref: SharedPreferences
    private lateinit var adapter: CategoryAdapter

    private val mainViewModel: MainViewModel by activityViewModels{
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).database)
    }

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
        //mainViewModel.insertMenuName(MenuNameListItem(null, "Завтрак", 0.0,  0.0,  0.0, 0.0))
        //mainViewModel.insertMenuName(MenuNameListItem(null, "Обед", 0.0,  0.0,  0.0, 0.0))
        //mainViewModel.insertMenuName(MenuNameListItem(null, "Ужин", 0.0,  0.0,  0.0, 0.0))
    }

    private fun initRcView(){
        pref = PreferenceManager.getDefaultSharedPreferences(requireActivity())
        val linear = getString(R.string.pref_linear)
        if (pref.getString(CommonConst.KEY_LINEAR, linear) == linear) form.rcViewProduct.layoutManager = LinearLayoutManager(activity)
        else form.rcViewProduct.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        val itemList = JsonHelper.getCategoryList("jsonCategoryProduct.json", requireContext())
        adapter = CategoryAdapter(requireContext(), pref, this )
        adapter.submitList(itemList)
        if (itemList.isEmpty()) form.tvEmpty.visibility = View.VISIBLE else form.tvEmpty.visibility = View.GONE
        form.rcViewProduct.adapter = adapter
    }

    companion object {
        const val PROD_CAT_KEY = "product_category_key"
        @JvmStatic
        fun newInstance() = FragmentProductCategory()
    }

    override fun itemClick(a: Any) {
        val slug = a as String
        val i = Intent(context, ProductListActivity::class.java).putExtra(
            FragmentProductCategory.PROD_CAT_KEY, slug)
        startActivity(i)
    }


}