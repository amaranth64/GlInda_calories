package ru.worklight64.calories.utils

import android.content.Context
import ru.worklight64.calories.entities.ItemProductClass

object DataContainerHelper {
    private var productSport = ArrayList<ItemProductClass>()

    fun getContainer(context: Context, slug: String):ArrayList<ItemProductClass>{

        if (slug == CommonConst.SLUG_SPORT) return getSportProduct(context)

        return ArrayList<ItemProductClass>()
    }

    private fun getSportProduct(context: Context):ArrayList<ItemProductClass>{
        if (productSport.isNotEmpty()) return productSport

        val file = CommonConst.SLUG_SPORT + ".json"
        productSport = JsonHelper.getProductList(file, context)
        return productSport
    }

}