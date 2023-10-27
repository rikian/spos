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

@Dao
interface UnitDao {
    @Query("SELECT * FROM units")
    fun getUnits(): List<UnitEntity>

    @Query("SELECT * FROM units WHERE id = :unitId")
    fun getUnit(unitId: Int): UnitEntity?

    @Insert
    fun insertUnit(data: UnitEntity)
}

@Dao
interface ProductDao {

}