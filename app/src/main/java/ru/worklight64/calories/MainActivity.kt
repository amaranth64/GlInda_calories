package ru.worklight64.calories

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import ru.worklight64.calories.databinding.ActivityMainBinding
import ru.worklight64.calories.fragments.FragmentCalc
import ru.worklight64.calories.fragments.FragmentManager
import ru.worklight64.calories.fragments.FragmentMyFood
import ru.worklight64.calories.fragments.FragmentProductCategory

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    
    private lateinit var form: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        form = ActivityMainBinding.inflate(layoutInflater)
        setContentView(form.root)

        init()

    }

    private fun init(){
        setSupportActionBar(form.mainContent.toolbar)
        // navigation toggle button
        val toggle = ActionBarDrawerToggle(this, form.drawerLayout, form.mainContent.toolbar,
            R.string.open, R.string.close)
        form.drawerLayout.addDrawerListener(toggle)
        toggle.drawerArrowDrawable.color = getColor(R.color.white)
        toggle.syncState()
        form.nvMain.setCheckedItem(R.id.id_nav_menu_calories)
        form.nvMain.setNavigationItemSelectedListener(this)

        FragmentManager.setFragment(FragmentProductCategory.newInstance(), this)
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId){
            R.id.id_nav_menu_calories -> {
                FragmentManager.setFragment(FragmentProductCategory.newInstance(), this)
            }
            R.id.id_nav_menu_myfood -> {
                FragmentManager.setFragment(FragmentMyFood.newInstance(), this)
            }
            R.id.id_nav_menu_calc -> {
                FragmentManager.setFragment(FragmentCalc.newInstance(), this)
            }
            R.id.id_nav_menu_settings -> {

            }
            R.id.id_nav_menu_about -> {

            }
            R.id.id_nav_menu_exit -> finish()
        }

        form.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    
}