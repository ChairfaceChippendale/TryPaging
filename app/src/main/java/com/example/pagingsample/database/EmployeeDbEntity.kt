package com.example.pagingsample.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employee")
data class EmployeeDbEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "position") val position: String,
    @ColumnInfo(name = "company_id") val companyId: Int,
    @ColumnInfo(name = "date") val date: Long
)