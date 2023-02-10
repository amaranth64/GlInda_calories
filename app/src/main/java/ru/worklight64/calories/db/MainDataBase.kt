package ru.worklight64.calories.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.worklight64.calories.entities.MenuNameListItem
import ru.worklight64.calories.entities.MenuProductListItem

@Database(entities = [MenuProductListItem::class, MenuNameListItem::class], version = 1)
abstract class MainDataBase: RoomDatabase() {

    abstract fun getDao():Dao

    companion object{

        @Volatile
        private var INSTANCE: MainDataBase? = null
        fun getDataBase(context: Context):MainDataBase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainDataBase::class.java,
                    "app_database.db"
                ).build()
                instance
            }
        }
    }

}