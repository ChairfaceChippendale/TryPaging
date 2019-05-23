package com.example.pagingsample.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single

@Dao
interface EmployeeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(employee: List<EmployeeDbEntity>)


    @Query("SELECT * FROM employee ORDER BY date DESC LIMIT :elementCount")
    fun getInitial(elementCount: Int): Single<List<EmployeeDbEntity>>


    @Query("SELECT * FROM employee WHERE date < :beforeDate ORDER BY date DESC LIMIT :elementCount")
    fun getBefore(elementCount: Int, beforeDate: Long): Single<List<EmployeeDbEntity>>

    @Query("SELECT * FROM employee WHERE date > :afterDate ORDER BY date DESC LIMIT :elementCount")
    fun getAfter(elementCount: Int, afterDate: Long): Single<List<EmployeeDbEntity>>

}