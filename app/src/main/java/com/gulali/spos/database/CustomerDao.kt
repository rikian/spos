package com.gulali.spos.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SposDao {
    // customers Dao
    @Query("SELECT * FROM customer ORDER BY id DESC")
    fun getCustomers(): List<CustomerEntity>
    @Insert
    fun insertCustomer(data: CustomerEntity)

    // unit Dao
    @Query("SELECT * FROM units")
    fun getUnits(): List<UnitEntity>

    @Query("SELECT * FROM units WHERE id = :unitId")
    fun getUnit(unitId: Int): UnitEntity?

    @Insert
    fun insertUnit(data: UnitEntity)

    // Product Dao
    @Query("SELECT * FROM products AS a INNER JOIN units AS b ON a.unit = b.id")
    fun getProducts(): List<ProductEntity>

    @Query(
        "SELECT a.id AS productID, a.image AS productImg, a.name AS productName, a.quantity AS productStock, b.name AS productUnit, a.updatedAt AS productUpdate FROM products AS a INNER JOIN units AS b ON a.unit = b.id"
    )
    fun getProductsForViewMenu(): List<ProductForViewMenu>

    @Insert
    fun insertProduct(data: ProductEntity)

    @Query(
        "SELECT a.id AS productID, a.image AS productImg, a.name AS productName, a.quantity AS productStock, b.name AS productUnit, a.updatedAt AS productUpdate FROM products AS a INNER JOIN units AS b ON a.unit = b.id WHERE a.name LIKE '%' || :query || '%'"
    )
    fun getProductByName(query: String): List<ProductForViewMenu>
}