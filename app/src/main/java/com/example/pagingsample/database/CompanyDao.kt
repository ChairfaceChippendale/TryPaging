package com.example.pagingsample.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CompanyDao {

    @Query("SELECT * FROM company")
    fun getAll(): List<CompanyDbEntity>

//    @Query("SELECT * FROM company WHERE id=:id")
//    fun getAllWithEmployess(id: Int): CompanyWithEmployeesDbEntity

    @Insert
    fun insert(company: CompanyDbEntity)

    @Insert
    fun insertAll(companyList: List<CompanyDbEntity>)

}