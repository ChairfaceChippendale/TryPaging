package com.example.pagingsample.database

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Single

@Dao
interface EmployeeDao {

    @Query("SELECT * FROM employee")
    fun getAll(): DataSource.Factory<Int, EmployeeDbEntity>

    @Insert
    fun insert(employee: EmployeeDbEntity)

    @Insert
    fun insertAll(employee: List<EmployeeDbEntity>)




    @Query("SELECT * FROM employee ORDER BY date LIMIT :elementCount")
    fun getFirst(elementCount: Int): Single<List<EmployeeDbEntity>>

}