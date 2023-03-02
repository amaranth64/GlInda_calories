package ru.worklight64.calories.utils

import android.content.Context
import android.widget.Toast
import org.json.JSONArray
import ru.worklight64.calories.R
import ru.worklight64.calories.entities.ItemCategoryClass
import ru.worklight64.calories.entities.ItemProductClass
import ru.worklight64.calories.entities.ItemSubCategoryClass
import java.io.IOException
import java.io.InputStream

object JsonHelper {

    fun getSubCategoryList(fileName: String, context: Context):ArrayList<ItemSubCategoryClass>{
        val list = ArrayList<ItemSubCategoryClass>()
        val jsonArray = JSONArray(getJsonText(fileName, context))

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            val name = obj.getString("name")
            val slug = obj.getString("slug")

            val item = ItemSubCategoryClass(name, slug)
            list.add(item)
        }
        return list
    }

    fun getCategoryList(fileName: String, context: Context):ArrayList<ItemCategoryClass>{
        val list = ArrayList<ItemCategoryClass>()
        val jsonArray = JSONArray(getJsonText(fileName, context))

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            val name = obj.getString("name")
            val slug = obj.getString("slug")
            val subcategory = obj.getString("subcategory")
            val type = obj.getInt("type")

            val item = ItemCategoryClass(name, slug, subcategory, type)
            list.add(item)
        }
        return list
    }
    fun getProductList(fileName: String, context: Context):ArrayList<ItemProductClass>{
        val list = ArrayList<ItemProductClass>()
        val jsonArray = JSONArray(getJsonText(fileName, context))

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            val name = obj.getString("title")
            val desc = obj.getString("description")
            val brand = obj.getString("brand")
            val category = obj.getString("category")
            val protein = obj.getDouble("protein")
            val carbo = obj.getDouble("carbo")
            val fat = obj.getDouble("fats")
            val energy = obj.getDouble("calories")
            val urlpicture = obj.getString("url-picture")
            val urlsite = obj.getString("url-site")
            val type = obj.getInt("type")
            val weight = obj.getDouble("weight")
            val slug = obj.getString("slug")

            val item = ItemProductClass(null, name, desc, brand, category, protein, carbo, fat, energy, urlpicture, urlsite, type, weight, 1, slug)
            list.add(item)
        }
        return list
    }
    private fun getJsonText(file:String, context: Context):String{
        var jsonFileData = "[]"
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