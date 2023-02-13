package ru.worklight64.calories

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ru.worklight64.calories.adapters.ProductListAdapter
import ru.worklight64.calories.databinding.ActivityProductListBinding
import ru.worklight64.calories.entities.ItemProductClass
import ru.worklight64.calories.utils.CommonConst
import ru.worklight64.calories.utils.JsonHelper

class ProductListActivity : AppCompatActivity(), ProductListAdapter.ProductListListener {

    private lateinit var adapter: ProductListAdapter
    private lateinit var form: ActivityProductListBinding
    private lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        form = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(form.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        initSharedPreferences()
        initRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //menuInflater.inflate(R.menu.new_note_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
         if (item.itemId == android.R.id.home){
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initSharedPreferences(){
        pref = PreferenceManager.getDefaultSharedPreferences(this)
        val linear = getString(R.string.pref_linear)
        if (pref.getString(CommonConst.KEY_LINEAR,linear) == linear) form.rcViewProduct.layoutManager = LinearLayoutManager(this)
        else form.rcViewProduct.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    private fun initRecyclerView(){
        val param = intent.getStringExtra(CommonConst.INTENT_SLUG).toString()
        val file = if (param.isEmpty()) "test.json" else "$param.json"
        val itemList = JsonHelper.getProductList(file, this)
        adapter = ProductListAdapter(this@ProductListActivity, pref)
        adapter.submitList(itemList)
        if (itemList.isEmpty()) form.tvEmpty.visibility = View.VISIBLE else form.tvEmpty.visibility = View.GONE
        form.rcViewProduct.adapter = adapter
    }

    override fun onClickItem(item: ItemProductClass) {

    }

    override fun addItem(item: ItemProductClass) {

    }

}