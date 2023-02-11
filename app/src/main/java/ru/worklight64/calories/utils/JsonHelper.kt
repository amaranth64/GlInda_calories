package ru.worklight64.calories.utils

import android.content.Context
import android.widget.Toast
import org.json.JSONArray
import ru.worklight64.calories.R
import ru.worklight64.calories.entities.ItemCategoryClass
import ru.worklight64.calories.entities.ItemProductClass
import java.io.IOException
import java.io.InputStream

object JsonHelper {

    fun getCategoryList(fileName: String, context: Context):ArrayList<ItemCategoryClass>{
        val list = ArrayList<ItemCategoryClass>()
        val jsonArray = JSONArray(getJsonText(fileName, context))

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            val name = obj.getString("name")
            val slug = obj.getString("slug")
            val type = obj.getInt("type")

            val item = ItemCategoryClass(name, slug, type)
            list.add(item)
        }
        return list
    }
    fun getProductList(fileName: String, context: Context):ArrayList<ItemProductClass>{
        val list = ArrayList<ItemProductClass>()
        val jsonArray = JSONArray(getJsonText(fileName, context))

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            val name = obj.getString("name")
            val protein = obj.getDouble("protein")
            val carbo = obj.getDouble("carbo")
            val fat = obj.getDouble("fat")
            val energy = obj.getDouble("energy")
            val slug = obj.getString("slug")

            val item = ItemProductClass(name, protein, carbo, fat, energy, slug)
            list.add(item)
        }
        return list
    }
    private fun getJsonText(file:String, context: Context):String{
        var jsonFileData: String = "[]"
        try {
            val inputStream: InputStream = context.assets.open(file)
            val size:Int = inputStream.available()
            val bytesArray = ByteArray(size)
            inputStream.read(bytesArray)
            jsonFileData = String(bytesArray)
        } catch (e: IOException){
            Toast.makeText(context,context.getString(R.string.error_db), Toast.LENGTH_LONG).show()
        }
        return jsonFileData
    }
}