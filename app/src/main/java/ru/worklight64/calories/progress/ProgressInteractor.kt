package ru.worklight64.calories.progress

import java.io.Serializable

class ProgressInteractor(var observer: Observer): Serializable {

    private var str = ""

    fun setStr(s: String){
        str = s
        observer.observe(str)
    }

    interface Observer{
        fun observe(s: String)
    }
}