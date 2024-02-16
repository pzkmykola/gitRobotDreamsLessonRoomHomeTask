package com.example.roomongit.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Entity
data class Todo (@PrimaryKey(autoGenerate = true) val id:Int? = null, val task:String, val progress: String)
@Entity
data class TodoNew (@PrimaryKey(autoGenerate = true) val id:Int? = null, val title:String, val note: String, val date: String )

@Dao
interface TodoDao {

    @Insert
    fun add(todo: Todo)

    @Delete
    fun delete(todo: Todo)

    @Query("SELECT * FROM todo")
    fun getAll(): LiveData<List<Todo>>
}

@Database(entities = [Todo::class,TodoNew::class], version = 1)
abstract class TodoDatabase: RoomDatabase(){
    abstract fun todoDao():TodoDao

    companion object{
        val MIGRATION_2_1 = object: Migration(2,1){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `todo` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `task` TEXT NOT NULL, `progress` TEXT NOT NULL)")
                database.execSQL("CREATE TABLE IF NOT EXISTS `todoNew` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `title` TEXT NOT NULL, `note` TEXT NOT NULL, `date` TEXT NOT NULL)")
            }
        }
    }
}