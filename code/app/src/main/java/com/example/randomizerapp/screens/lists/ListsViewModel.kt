package com.example.randomizerapp.screens.lists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.randomizerapp.data.ListItem
import com.example.randomizerapp.data.ListsRepository
import com.example.randomizerapp.data.UserList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ListsViewModel(private val repository: ListsRepository) : ViewModel() {

    private val _lists = MutableStateFlow<List<UserList>>(emptyList())
    val lists = _lists.asStateFlow()

    init {
        _lists.value = repository.loadLists()
    }

    private fun save() {
        repository.saveLists(_lists.value)
    }

    fun addList(name: String) {
        val newList = UserList(name = name)
        _lists.update { it + newList }
        save()
    }

    fun deleteList(listId: Long) {
        _lists.update { allLists -> allLists.filter { it.id != listId } }
        save()
    }

    fun addItem(listId: Long, text: String) {
        _lists.update { allLists ->
            allLists.map { list ->
                if (list.id == listId) {
                    list.copy(items = list.items + ListItem(text = text))
                } else {
                    list
                }
            }
        }
        save()
    }

    fun deleteItem(listId: Long, itemId: Long) {
        _lists.update { allLists ->
            allLists.map { list ->
                if (list.id == listId) {
                    list.copy(items = list.items.filter { it.id != itemId })
                } else {
                    list
                }
            }
        }
        save()
    }
}

// Фабрика для создания ViewModel вручную, так как у нас нет Hilt
class ListsViewModelFactory(private val repository: ListsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ListsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}