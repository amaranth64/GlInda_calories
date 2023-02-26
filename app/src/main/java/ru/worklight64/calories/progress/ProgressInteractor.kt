package ru.worklight64.calories.progress

import ru.worklight64.calories.entities.ItemProductClass
import java.io.Serializable

class ProgressInteractor(var observer: Observer): Serializable {

    private var str = ""

    var product_item: ItemProductClass = ItemProductClass(null,"","","","", 0.0,0.0,0.0,0.0,"","",0,0.0,"")
    var category_slug: String = ""

    fun setStep(s: String){
        str = s
        observer.observe(str)
    }

    interface Observer{
        fun observe(s: String)
    }
}