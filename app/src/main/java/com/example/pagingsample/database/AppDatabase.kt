package com.example.pagingsample.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [EmployeeDbEntity::class, CompanyDbEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun employeeDao(): EmployeeDao
    abstract fun companyDao(): CompanyDao
}