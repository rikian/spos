package com.gulali.spos.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CustomerDao {
    @Query("SELECT * FROM customer ORDER BY id DESC")
    fun getCustomers(): List<CustomerEntity>

    @Insert
    fun insertCustomer(data: CustomerEntity)
}