package ru.worklight64.calories.utils

import android.content.Context
import ru.worklight64.calories.entities.ItemProductClass

object DataContainerHelper {
    private var productSport = ArrayList<ItemProductClass>()
    private var productMilkDairy = ArrayList<ItemProductClass>()

    fun getContainer(context: Context, category: String):ArrayList<ItemProductClass>{

        if (category == CommonConst.SLUG_SPORT) return getSportProduct(context)
        if (category == CommonConst.SLUG_MILK_DAIRY) return getMilkDairytProduct(context)

        return ArrayList<ItemProductClass>()
    }

    fun productInContainer(context: Context, category: String, slug: String): ItemProductClass{
        val data = getContainer(context, category)
        val product = data.find { it.slug == slug }
        if (product != null) return product
        return ItemProductClass(null,"","","","", 0.0,0.0,0.0,0.0,"","",0,0.0,0,"")
    }


    private fun getSportProduct(context: Context):ArrayList<ItemProductClass>{
        if (productSport.isNotEmpty()) return productSport

        val file = CommonConst.SLUG_SPORT + ".json"
        productSport = JsonHelper.getProductList(file, context)
        return productSport
    }

    private fun getMilkDairytProduct(context: Context):ArrayList<ItemProductClass>{
        if (productMilkDairy.isNotEmpty()) return productMilkDairy

        val file = CommonConst.SLUG_MILK_DAIRY + ".json"
        productMilkDairy = JsonHelper.getProductList(file, context)
        return productMilkDairy
    }

}