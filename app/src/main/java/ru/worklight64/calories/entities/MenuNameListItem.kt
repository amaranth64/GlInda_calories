package ru.worklight64.calories.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "menu_names_list")
data class MenuNameListItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "protein")
    val protein: Double,
    @ColumnInfo(name = "carbo")
    val carbo: Double,
    @ColumnInfo(name = "fat")
    val fat: Double,
    @ColumnInfo(name = "energy")
    val energy: Double
): Serializable
