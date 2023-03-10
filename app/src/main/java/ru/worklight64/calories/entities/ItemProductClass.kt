package ru.worklight64.calories.entities



data class ItemProductClass(
    val id: Int? = null,
    val title: String,
    val description: String,
    val brand: String,
    val class_of_product: String,
    val protein: Double,
    val carbo: Double,
    val fat: Double,
    val energy: Double,
    val url_picture: String,
    val url_site: String,
    val type: Int,
    val weight: Double,
    val count: Int,
    val slug: String
)
