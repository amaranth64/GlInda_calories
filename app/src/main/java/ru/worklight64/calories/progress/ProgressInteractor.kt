package ru.worklight64.calories.progress

import ru.worklight64.calories.entities.ItemProductClass
import java.io.Serializable

class ProgressInteractor(var observer: Observer): Serializable {

    private val steps = ProgSteps.values()
    private var currentStep = 0


    var product_item: ItemProductClass = ItemProductClass(null,"","","","", 0.0,0.0,0.0,0.0,"","",0,0.0,"")
    var category_slug: String = ""
    var menuID = 0
    var product_weight = 0.0
    var product_count= 0

    fun getCurrentStep():ProgSteps{
        return ProgSteps.valueOf(steps[currentStep].toString())
    }
    fun next(){
        currentStep++
        if (currentStep >= (steps.size - 1))  currentStep = steps.size - 1
        else observer.observe(ProgSteps.valueOf(steps[currentStep].toString()))
    }

    fun prev(){
        currentStep--
        if (currentStep < 0)  currentStep = 0
        else observer.observe(ProgSteps.valueOf(steps[currentStep].toString()))
    }

    fun setStep(s: ProgSteps){
        currentStep = s.ordinal
        observer.observe(ProgSteps.valueOf(steps[currentStep].toString()))
    }

    interface Observer{
        fun observe(s: ProgSteps)
    }
}