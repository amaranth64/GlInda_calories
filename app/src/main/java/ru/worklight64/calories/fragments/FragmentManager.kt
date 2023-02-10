package ru.worklight64.calories.fragments

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ru.worklight64.calories.R

object FragmentManager {
    var currentFragment: Fragment? = null

    fun setFragment(newFragment: Fragment, activity:AppCompatActivity){
        val transaction = activity.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.placeHolder, newFragment)
        transaction.commit()
        currentFragment = newFragment
    }
}