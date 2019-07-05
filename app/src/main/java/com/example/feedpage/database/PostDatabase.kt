package com.example.feedpage.database

import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Database
import android.content.Context


@Database(entities = [Post::class], version = 1)
abstract class PostDatabase : RoomDatabase() {

    abstract fun postDao(): PostDao

    companion object {

        private var INSTANCE: PostDatabase? = null

        fun getAppDatabase(context: Context): PostDatabase {
            if (INSTANCE == null) {
                INSTANCE =
                    Room.databaseBuilder(context.applicationContext, PostDatabase::class.java, "post-database")
                        // allow queries on the main thread.
                        // Don't do this on a real app! See PersistenceBasicSample for an example.
                        .allowMainThreadQueries()
                        .build()
            }
            return INSTANCE as PostDatabase
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}