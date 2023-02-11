package ru.worklight64.calories.entities



data class ItemProductClass(
    val name: String,
    val protein: Double,
    val carbo: Double,
    val fat: Double,
    val energy: Double,
    val slug: String
)
