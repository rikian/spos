package com.gulali.spos.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "customer")
data class CustomerEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val phone: String,
    val createdAt: String,
    val updatedAt: String,
)

@Entity(tableName = "units")
data class UnitEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val createdAt: String,
    val updatedAt: String,
)

@Entity(
    tableName = "products",
    foreignKeys = [ForeignKey(entity = UnitEntity::class, parentColumns = ["id"], childColumns = ["unit"])]
)
data class ProductEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var image: String,
    var name: String,
    var quantity: Int,
    var unit: Int, // This column references the "name" column in the UnitEntity
    var purchase: Int,
    var price: Int,
    var createdAt: String,
    var updatedAt: String,
)