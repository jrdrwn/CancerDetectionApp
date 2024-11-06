package com.dicoding.asclepius.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ClassificationEntity::class], version = 1)
abstract class ClassificationRoomDatabase : RoomDatabase() {
    abstract fun classificationDao(): ClassificationDao

    companion object {
        @Volatile
        private var INSTANCE: ClassificationRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): ClassificationRoomDatabase {
            if (INSTANCE == null) {
                synchronized(ClassificationRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        ClassificationRoomDatabase::class.java,
                        "classification_database"
                    ).build()
                }
            }
            return INSTANCE as ClassificationRoomDatabase
        }
    }
}