package ru.worklight64.calories.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.worklight64.calories.entities.MenuNameListItem
import ru.worklight64.calories.entities.MenuProductListItem

@Dao
interface Dao {
    //===========================================
    @Query("SELECT * FROM menu_names_list")
    fun getAllMenuName(): Flow<List<MenuNameListItem>>

    @Query("SELECT * FROM menu_names_list WHERE id IS :id")
    fun getMenuName(id: Int): Flow<List<MenuNameListItem>>
    @Query("DELETE FROM menu_names_list WHERE id IS :id")
    suspend fun deleteMenu(id: Int)
    @Insert
    suspend fun insertMenu(item: MenuNameListItem)
    @Update
    suspend fun updateMenu(item: MenuNameListItem)
    //===========================================
    @Query("SELECT * FROM menu_product_list WHERE menu_id LIKE :menuID")
    fun getAllMenuProductListItems(menuID: Int): Flow<List<MenuProductListItem>>
    @Insert
    suspend fun insertProductToMenu(item: MenuProductListItem)
    @Update
    suspend fun updateProductInMenu(item: MenuProductListItem)
    @Query("DELETE FROM menu_product_list WHERE id IS :id")
    suspend fun deleteProductInMenu(id: Int)
    @Query("DELETE FROM menu_product_list WHERE menu_id LIKE :menuID")
    suspend fun deleteAllProductsFromMenu(menuID: Int)
}