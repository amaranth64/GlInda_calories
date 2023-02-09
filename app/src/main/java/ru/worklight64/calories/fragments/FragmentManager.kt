package ru.worklight64.calories.fragments

import androidx.appcompat.app.AppCompatActivity
import ru.worklight64.calories.R

object FragmentManager {
    var currentFragment: BaseFragment? = null

    fun setFragment(newFragment: BaseFragment, activity:AppCompatActivity){
        val transaction = activity.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.rvView, newFragment)
        transaction.commit()
        currentFragment = newFragment
    }
}