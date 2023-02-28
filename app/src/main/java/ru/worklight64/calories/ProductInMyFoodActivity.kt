package ru.worklight64.calories

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ru.worklight64.calories.adapters.ProductInMenuAdapter
import ru.worklight64.calories.adapters.RecyclerTouchListener
import ru.worklight64.calories.databinding.ActivityProductInMyfoodBinding
import ru.worklight64.calories.db.MainViewModel
import ru.worklight64.calories.entities.ItemProductClass
import ru.worklight64.calories.entities.MenuNameListItem
import ru.worklight64.calories.utils.CommonConst
import ru.worklight64.calories.utils.DataContainerHelper
import java.io.Serializable

class ProductInMyFoodActivity : AppCompatActivity(), ProductInMenuAdapter.ProductInMenuListener {
    lateinit var form: ActivityProductInMyfoodBinding
    private lateinit var menu:MenuNameListItem
    private lateinit var adapter: ProductInMenuAdapter
    private lateinit var pref: SharedPreferences

    private val mainViewModel: MainViewModel by viewModels{
        MainViewModel.MainViewModelFactory((applicationContext as MainApp).database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        form = ActivityProductInMyfoodBinding.inflate(layoutInflater)
        setContentView(form.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initSharedPreferences()
        initRecyclerView()
        observer()

        form.bAddProduct.setOnClickListener {
            val i = Intent(this, StepByStepActivity::class.java).putExtra(CommonConst.INTENT_MENU, menu.id)
            startActivity(i)
        }
    }

    private fun <T : Serializable?> getSerializable(intent: Intent, key: String, className: Class<T>): T {
        return if (Build.VERSION.SDK_INT >= 33)
            intent.getSerializableExtra(key, className)!!
        else
            intent.getSerializableExtra(key) as T
    }

    private fun observer() {
        menu = getSerializable(intent, CommonConst.INTENT_MENU, MenuNameListItem::class.java)
        mainViewModel.allProductInMenuList(menu.id!!).observe(this) { menu_item ->
            val items = ArrayList<ItemProductClass>()
            var protein = 0.0
            var carbo = 0.0
            var fat = 0.0
            var kcal = 0.0
            menu_item.forEach {
                val product = DataContainerHelper.productInContainer(this, it.category, it.slug)
                items.add(product.copy(id = it.id))
                protein += product?.protein!! * it.weight / 100
                carbo += product?.carbo!! * it.weight / 100
                fat += product?.fat!! * it.weight / 100
                kcal += product?.energy!! * it.weight / 100
            }

            mainViewModel.updateMenuName(
                menu.copy(
                    protein = protein,
                    carbo = carbo,
                    fat = fat,
                    energy = kcal
                )
            )

            adapter.submitList(items)
            if (items.isEmpty()) form.tvEmpty.visibility =
                View.VISIBLE else form.tvEmpty.visibility = View.GONE
        }

    }
    private fun initSharedPreferences(){
        pref = PreferenceManager.getDefaultSharedPreferences(this)
        val linear = getString(R.string.pref_linear)
        if (pref.getString(CommonConst.KEY_LINEAR,linear) == linear) form.rcViewProduct.layoutManager = LinearLayoutManager(this)
        else form.rcViewProduct.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    private fun initRecyclerView(){
        adapter = ProductInMenuAdapter(this@ProductInMyFoodActivity, pref)
        form.rcViewProduct.adapter = adapter

        val touchListener = RecyclerTouchListener(this, form.rcViewProduct)
        touchListener.setClickable(object : RecyclerTouchListener.OnRowClickListener {
            override fun onRowClicked(position: Int) {
                Toast.makeText(
                    applicationContext,
                    "taskList.get(position).getName()",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onIndependentViewClicked(independentViewID: Int, position: Int) {}
        })
            .setSwipeOptionViews(R.id.delete_task, R.id.edit_task)
            .setSwipeable(R.id.rowFG, R.id.rowBG,
                RecyclerTouchListener.OnSwipeOptionsClickListener { viewID, position ->
                    when (viewID) {
                        R.id.delete_task -> {
                            val a = adapter.currentList[position]
                            mainViewModel.deleteProductInMenu(a.id!!)
                        }
                        R.id.edit_task -> {
                            Toast.makeText(
                                applicationContext,
                                "Edit",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                })

        form.rcViewProduct.addOnItemTouchListener(touchListener)
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

    override fun onClickItem(item: ItemProductClass) {

    }

    override fun deleteItem(item: ItemProductClass) {

    }
}