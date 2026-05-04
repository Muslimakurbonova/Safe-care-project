package com.programmsoft.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.programmsoft.room.dao.ContentDataCategoryDao
import com.programmsoft.room.dao.ContentDataDao
import com.programmsoft.room.entity.ContentData
import com.programmsoft.room.entity.ContentDataCategory

@Database(
    entities = [ContentData::class, ContentDataCategory::class],
    version = 1
)
abstract class RoomDB : RoomDatabase(){
    abstract fun contentDataDao(): ContentDataDao
    abstract fun contentDataCategoryDao(): ContentDataCategoryDao

    companion object {
        private var instance: RoomDB? = null

        @Synchronized
        fun getInstance(context: Context): RoomDB {
            if (instance == null) {
                instance =
                    Room.databaseBuilder(
                        context,
                        RoomDB::class.java,
                        "contents_db"
                    )
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
            }
            return instance!!
        }
    }
}
