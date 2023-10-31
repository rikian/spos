package com.gulali.spos.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [CustomerEntity::class, ProductEntity::class, UnitEntity::class],
    version = 1
)
abstract class SposDB: RoomDatabase() {
    abstract fun sposDAO(): SposDao

    companion object {
        @Volatile
        private var INSTANCE: SposDB? = null

        fun getSposDatabase(context: Context): SposDB {
            val tmplInstant = INSTANCE
            if (tmplInstant != null) {
                return tmplInstant
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SposDB::class.java,
                    "spos"
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}