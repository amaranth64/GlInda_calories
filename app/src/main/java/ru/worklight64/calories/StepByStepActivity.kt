package ru.worklight64.calories

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import ru.worklight64.calories.databinding.ActivityStepByStepBinding
import ru.worklight64.calories.db.MainViewModel
import ru.worklight64.calories.entities.MenuProductListItem
import ru.worklight64.calories.fragments.FragmentManager
import ru.worklight64.calories.progress.*
import ru.worklight64.calories.utils.CommonConst
import ru.worklight64.calories.utils.DataContainerHelper

class StepByStepActivity : AppCompatActivity(), ProgressInteractor.Observer{
    private lateinit var form: ActivityStepByStepBinding
    private lateinit var interactor: ProgressInteractor
    private var menuID = 0

    private val mainViewModel: MainViewModel by viewModels{
        MainViewModel.MainViewModelFactory((applicationContext as MainApp).database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        form = ActivityStepByStepBinding.inflate(layoutInflater)
        setContentView(form.root)
        setSupportActionBar(form.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        interactor = ProgressInteractor(this)
        menuID = intent.getIntExtra(CommonConst.INTENT_MENU, 0)
        interactor.menuID = menuID
        interactor.setStep(ProgSteps.CATEGORY)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //menuInflater.inflate(R.menu.new_note_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            if (interactor.getCurrentStep() == ProgSteps.CATEGORY) finish()
            else {
                interactor.prev()
            }
        }

        return super.onOptionsItemSelected(item)
    }


    override fun observe(s: ProgSteps) {
        when (s){
            ProgSteps.CATEGORY -> {
                form.tvStep1.background =  resources.getDrawable(R.color.card_protein)
                FragmentManager.setFragment(FragProgCategory.newInstance(interactor), this)
            }
            ProgSteps.PRODUCT -> {
                form.tvStep2.setBackgroundColor(resources.getColor(R.color.card_protein))
                FragmentManager.setFragment(FragProgProduct.newInstance(interactor), this)
            }
            ProgSteps.WEIGHT -> {
                form.tvStep3.setBackgroundColor(resources.getColor(R.color.card_protein))
                FragmentManager.setFragment(FragProgWeight.newInstance(interactor), this)
            }
            ProgSteps.FINAL -> {
                form.tvStep4.setBackgroundColor(resources.getColor(R.color.card_protein))
                FragmentManager.setFragment(FragProgFinal.newInstance(interactor), this)
            }
            ProgSteps.DONE -> {


                itemAddToDB()

            }
        }
    }

    private fun itemAddToDB() {

        val product = interactor.product_item
        val slug: String = product.slug
        val weight: Double = interactor.product_weight.toDouble()
        val count: Int = interactor.product_count.toInt()
        val menuID: Int = interactor.menuID

        mainViewModel.insertProductToMenu(
            MenuProductListItem(
                null,
                interactor.category_slug,
                slug,
                weight,
                count,
                menuID)
        )

        // рассчитать новые значения для меню

        mainViewModel.getMenuName(menuID).observe(this){currentMenu->

            mainViewModel.getMenuName(menuID).removeObservers(this)

            mainViewModel.allProductInMenuList(menuID).observe(this){ menu_list->
                mainViewModel.allProductInMenuList(menuID).removeObservers(this)
                var protein = 0.0
                var carbo = 0.0
                var fat = 0.0
                var kcal = 0.0
                menu_list.forEach{menu_item->

                    val i = DataContainerHelper.getContainer(this, menu_item.category)

                    val product = i.find {
                        it.slug ==  menu_item.slug
                    }

                    if (product?.title!!.isNotEmpty()){

                        protein += product.protein * menu_item.weight / 100
                        carbo += product.carbo * menu_item.weight / 100
                        fat += product.fat * menu_item.weight / 100
                        kcal += product.energy * menu_item.weight / 100

                    }

                }
                mainViewModel.updateMenuName(currentMenu[0].copy(protein = protein, carbo = carbo, fat = fat, energy =  kcal))
                finish()
            }

        }


    }

}