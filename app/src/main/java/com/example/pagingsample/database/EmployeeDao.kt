package com.example.pagingsample.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface EmployeeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(employee: List<EmployeeDbEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(employee: EmployeeDbEntity)



    @Query("SELECT * FROM employee ORDER BY date DESC LIMIT :elementCount")
    fun getInitial(elementCount: Int): Single<List<EmployeeDbEntity>>

    @Query("SELECT * FROM employee WHERE date > :beforeDate ORDER BY date DESC LIMIT :elementCount")
    fun getBefore(elementCount: Int, beforeDate: Long): Single<List<EmployeeDbEntity>>

    @Query("SELECT * FROM employee WHERE date < :afterDate ORDER BY date DESC LIMIT :elementCount")
    fun getAfter(elementCount: Int, afterDate: Long): Single<List<EmployeeDbEntity>>





    @Query("SELECT * FROM employee ORDER BY date DESC LIMIT :initCount")
    fun getLimit(initCount: Int): Flowable<List<EmployeeDbEntity>>

    @Query("SELECT * FROM employee ORDER BY date DESC LIMIT :initCount OFFSET :offset")
    fun getLimitOffset(initCount: Int, offset: Int): Flowable<List<EmployeeDbEntity>>

    @Query("SELECT * FROM employee WHERE date >= :from AND date <= :to ORDER BY date DESC")
    fun getRange(from: Long = 0, to: Long): Flowable<List<EmployeeDbEntity>>



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateElement(employee: EmployeeDbEntity): Completable

    @Query("SELECT * FROM employee WHERE id == :id")
    abstract fun getById(id: String): Single<EmployeeDbEntity>

    @Query("SELECT id FROM employee LIMIT 1")
    abstract fun observe(): Flowable<List<String>>

}