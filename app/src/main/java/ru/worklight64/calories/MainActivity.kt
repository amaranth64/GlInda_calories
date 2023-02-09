package ru.worklight64.calories

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import ru.worklight64.calories.databinding.ActivityMainBinding

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
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId){
            R.id.id_nav_menu_calories -> {

            }
            R.id.id_nav_menu_myfood -> {

            }
            R.id.id_nav_menu_calc -> {

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