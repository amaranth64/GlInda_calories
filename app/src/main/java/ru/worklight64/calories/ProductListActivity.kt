package ru.worklight64.calories

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ru.worklight64.calories.adapters.ProductListAdapter
import ru.worklight64.calories.databinding.ActivityProductListBinding
import ru.worklight64.calories.db.MainViewModel
import ru.worklight64.calories.dialogs.AddProductToMenuDialog
import ru.worklight64.calories.entities.ItemProductClass
import ru.worklight64.calories.entities.MenuProductListItem
import ru.worklight64.calories.fragments.FragmentProductCategory
import ru.worklight64.calories.utils.CommonConst
import ru.worklight64.calories.utils.DataContainerHelper

class ProductListActivity : AppCompatActivity(), ProductListAdapter.ProductListListener, AddProductToMenuDialog.Listener {

    private lateinit var adapter: ProductListAdapter
    private lateinit var form: ActivityProductListBinding
    private lateinit var pref: SharedPreferences

    private var currentCategory: String = ""

    private val mainViewModel: MainViewModel by viewModels{
        MainViewModel.MainViewModelFactory((applicationContext as MainApp).database)
    }

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
        currentCategory = intent.getStringExtra(FragmentProductCategory.PROD_CAT_KEY).toString()
        //val file = if (currentCategory.isEmpty()) "test.json" else "$currentCategory.json"

        val itemList = DataContainerHelper.getContainer(this, currentCategory)

        //val itemList = JsonHelper.getProductList(file, this)
        adapter = ProductListAdapter(this@ProductListActivity, pref)
        adapter.submitList(itemList)
        if (itemList.isEmpty()) form.tvEmpty.visibility = View.VISIBLE else form.tvEmpty.visibility = View.GONE
        form.rcViewProduct.adapter = adapter
    }

    override fun onClickItem(item: ItemProductClass) {

    }

    override fun addItem(item: ItemProductClass) {
       mainViewModel.allMenuNames.observe(this){
           mainViewModel.allMenuNames.removeObservers(this)
           AddProductToMenuDialog.showDialog(this, this, item, it)
       }

    }

    override fun onAdd(slug: String, count: Int, menuID: Int) {
        mainViewModel.insertProductToMenu(
            MenuProductListItem(
                null,
                currentCategory,
                slug,
                count,
                menuID))

        // рассчитать новые значения для меню

        mainViewModel.getMenuName(menuID).observe(this){currentMenu->

            mainViewModel.getMenuName(menuID).removeObservers(this)

            mainViewModel.getAllProductMenuList(menuID).observe(this){menu_list->
                mainViewModel.getAllProductMenuList(menuID).removeObservers(this)
                var protein = 0.0
                var carbo = 0.0
                var fat = 0.0
                var kcal = 0.0
                menu_list.forEach{menu_item->
                    val i = DataContainerHelper.getContainer(this, menu_item.category)
                    val product = i.find {
                        it.slug ==  menu_item.slug
                    }
                    protein += product?.protein!!
                    carbo += product?.carbo!!
                    fat += product?.fat!!
                    kcal += product?.energy!!
                }
                mainViewModel.updateMenuName(currentMenu[0].copy(protein = protein, carbo =carbo, fat = fat, energy =  kcal))

            }

        }


    }

}