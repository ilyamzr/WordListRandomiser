package com.example.randomizerapp.data

import androidx.room.*
import androidx.room.Entity
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

// --- Сущности (Таблицы) ---

@Entity(tableName = "lists")
data class ListEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String
)

@Entity(tableName = "items",
    foreignKeys = [ForeignKey(
        entity = ListEntity::class,
        parentColumns = ["id"],
        childColumns = ["listId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("listId")]
)
data class ItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val text: String,
    val listId: Long
)

// --- Класс для связывания списка с его элементами ---

data class ListWithItems(
    @Embedded val list: ListEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "listId"
    )
    val items: List<ItemEntity>
)

// --- DAO (Data Access Object) - Интерфейс для запросов к БД ---

@Dao
interface ListsDao {
    @Transaction
    @Query("SELECT * FROM lists")
    fun getAllListsWithItems(): Flow<List<ListWithItems>>

    @Query("SELECT * FROM lists WHERE id = :listId")
    fun getListById(listId: Long): Flow<ListWithItems>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: ListEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ItemEntity)

    @Delete
    suspend fun deleteList(list: ListEntity)

    @Delete
    suspend fun deleteItem(item: ItemEntity)
}

// --- Основной класс базы данных ---

@Database(entities = [ListEntity::class, ItemEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun listsDao(): ListsDao
}