package ru.worklight64.calories.entities

data class ItemCategoryClass(
    val name: String,
    val slug: String,
    val subcategory: String,
    val type: Int,
    var expanded: Boolean = false,
)
