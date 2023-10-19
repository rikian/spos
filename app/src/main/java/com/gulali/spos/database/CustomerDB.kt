package com.gulali.spos.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [CustomerEntity::class],
    version = 1
)
abstract class CustomerDB: RoomDatabase() {
    abstract fun customerDAO(): CustomerDao

    companion object {
        @Volatile
        private var INSTANCE: CustomerDB? = null

        fun getCustomerDatabase(context: Context): CustomerDB {
            val tmplInstant = INSTANCE
            if (tmplInstant != null) {
                return tmplInstant
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CustomerDB::class.java,
                    "customers"
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