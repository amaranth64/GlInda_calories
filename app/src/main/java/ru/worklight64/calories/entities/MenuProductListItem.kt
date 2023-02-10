package ru.worklight64.calories.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu_product_list")
data class MenuProductListItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "slug")
    val slug: String,
    @ColumnInfo(name = "menu_id")
    val menuID: String
)
