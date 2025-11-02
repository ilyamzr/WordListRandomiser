/*package com.example.randomizerapp.di

import android.content.Context
import androidx.room.Room
import com.example.randomizerapp.data.AppDatabase
import com.example.randomizerapp.data.ListsDao
import com.google.android.datatransport.runtime.dagger.Module
import com.google.android.datatransport.runtime.dagger.Provides
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "randomizer_database"
        ).build()
    }

    @Provides
    fun provideListsDao(database: AppDatabase): ListsDao {
        return database.listsDao()
    }
}*/