package com.example.randomizerapp.data

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.IOException

// --- Модели данных для сохранения ---

@Serializable
data class ListItem(
    val id: Long = System.currentTimeMillis(), // Уникальный ID
    val text: String
)

@Serializable
data class UserList(
    val id: Long = System.currentTimeMillis(), // Уникальный ID
    val name: String,
    val items: List<ListItem> = emptyList()
)

// --- Класс для работы с файлом ---

class ListsRepository(private val context: Context) {

    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }
    private val file = File(context.filesDir, "user_lists.json")

    // Загрузить все списки из файла
    fun loadLists(): List<UserList> {
        return try {
            if (file.exists()) {
                val content = file.readText()
                json.decodeFromString(content)
            } else {
                emptyList()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            emptyList()
        }
    }

    // Сохранить все списки в файл
    fun saveLists(lists: List<UserList>) {
        try {
            val content = json.encodeToString(lists)
            file.writeText(content)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}