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

@Database(
    entities = [UnitEntity::class],
    version = 1
)
abstract class UnitDB: RoomDatabase() {
    abstract fun unitDao(): UnitDao

    companion object {
        @Volatile
        private var INSTANCE: UnitDB? = null

        fun getCustomerDatabase(context: Context): UnitDB {
            val tmplInstant = INSTANCE
            if (tmplInstant != null) {
                return tmplInstant
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UnitDB::class.java,
                    "units"
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