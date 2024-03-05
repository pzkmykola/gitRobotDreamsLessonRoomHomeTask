package com.example.roomongit.Modules

import android.content.Context
import androidx.room.Room
import com.example.roomongit.TodoRepositoryImplementation
import com.example.roomongit.dbnew.TodoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class Application {
    @Provides
    @Singleton
    fun getRepository(@ApplicationContext context: Context): TodoRepository {
        val dbnew = Room.databaseBuilder(context, TodoDatabase::class.java, "todo_database").build()
        return TodoRepositoryImplementation(dbnew)
    }
}