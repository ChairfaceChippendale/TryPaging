package com.example.pagingsample.database

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface EmployeeDao {

    @Query("SELECT * FROM employee")
    fun getAll(): DataSource.Factory<Int, EmployeeDbEntity>

    @Insert
    fun insert(employee: EmployeeDbEntity)

    @Insert
    fun insertAll(employee: List<EmployeeDbEntity>)

}