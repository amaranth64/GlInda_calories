package ru.worklight64.calories

import android.app.Application
import ru.worklight64.calories.db.MainDataBase

class MainApp: Application(){
    val database by lazy { MainDataBase.getDataBase(this)}
}