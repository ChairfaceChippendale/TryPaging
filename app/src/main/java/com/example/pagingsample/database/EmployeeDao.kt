package com.example.pagingsample.database

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EmployeeDao {

    @Query("SELECT * FROM employee ORDER BY date DESC")
    fun getAll(): DataSource.Factory<Int, EmployeeDbEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(employee: List<EmployeeDbEntity>)



}