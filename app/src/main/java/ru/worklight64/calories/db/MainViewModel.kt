package ru.worklight64.calories.db

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.worklight64.calories.entities.MenuNameListItem
import ru.worklight64.calories.entities.MenuProductListItem

class MainViewModel(database: MainDataBase): ViewModel() {

    private val dao = database.getDao()

    //==============================================
    val allMenuNames: LiveData<List<MenuNameListItem>> = dao.getAllMenuName().asLiveData()
    fun insertMenuName(item: MenuNameListItem) = viewModelScope.launch {
        dao.insertMenuName(item)
    }
    fun updateMenuName(item: MenuNameListItem) = viewModelScope.launch {
        dao.updateMenuName(item)
    }
    fun deleteMenuName(id: Int) = viewModelScope.launch {
        dao.deleteMenuName(id)
    }

    //==============================================

    fun insertProductToMenu(item: MenuProductListItem) = viewModelScope.launch {
        dao.insertMenuProductItem(item)
    }


    class MainViewModelFactory(private val database: MainDataBase): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(database) as T
            }
            throw IllegalArgumentException("Unknown ViewModelClass")
        }
    }
}