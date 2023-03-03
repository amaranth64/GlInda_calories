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
import ru.worklight64.calories.databinding.ActivityMyfoodLayoutBinding
import ru.worklight64.calories.db.MainViewModel
import ru.worklight64.calories.dialogs.EditProductInMenuDialog
import ru.worklight64.calories.entities.ItemProductClass
import ru.worklight64.calories.entities.MenuNameListItem
import ru.worklight64.calories.entities.MenuProductListItem
import ru.worklight64.calories.utils.CommonConst
import ru.worklight64.calories.utils.DataContainerHelper
import java.io.Serializable

class ProductInMyFoodActivity : AppCompatActivity(), EditProductInMenuDialog.Listener{
    lateinit var form: ActivityMyfoodLayoutBinding
    private lateinit var menu:MenuNameListItem
    private lateinit var adapter: ProductInMenuAdapter
    private lateinit var pref: SharedPreferences
    private lateinit var productsInMenu: ArrayList<MenuProductListItem>

    private val mainViewModel: MainViewModel by viewModels{
        MainViewModel.MainViewModelFactory((applicationContext as MainApp).database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        form = ActivityMyfoodLayoutBinding.inflate(layoutInflater)
        setContentView(form.root)
        setSupportActionBar(findViewById(R.id.toolbar))
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
        form.toolbarLayout.title = menu.name
        mainViewModel.allProductInMenuList(menu.id!!).observe(this) { menu_item ->
            productsInMenu = menu_item as ArrayList<MenuProductListItem>
            val items = ArrayList<ItemProductClass>()
            var protein = 0.0
            var carbo = 0.0
            var fat = 0.0
            var kcal = 0.0
            menu_item.forEach {
                val product = DataContainerHelper.productInContainer(this, it.category, it.slug)

                val p = product?.protein!! * it.weight / 100
                val c = product?.carbo!! * it.weight / 100
                val f = product?.fat!! * it.weight / 100
                val e = product?.energy!! * it.weight / 100

                items.add(product.copy(id = it.id, protein = p, carbo = c, fat = f, energy = e, weight = it.weight, count = it.count))

                protein += p
                carbo += c
                fat += f
                kcal += e

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
            form.pbLoading.visibility = View.GONE
            form.tvProtein.text = "%.1f".format(protein)
            form.tvCarbo.text = "%.1f".format(carbo)
            form.tvFats.text = "%.1f".format(fat)
            form.tvEnergy.text = "%.1f".format(kcal)

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

        adapter = ProductInMenuAdapter(pref)
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
                            val a = adapter.currentList[position]
                            val productInMenu = productsInMenu.find { it.slug == a.slug }
                            if (productInMenu != null)
                            EditProductInMenuDialog.showDialog(this@ProductInMyFoodActivity, this@ProductInMyFoodActivity, a, productInMenu)
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

    override fun onAdd(item: MenuProductListItem) {
        mainViewModel.updateProductToMenu(item)
    }

}