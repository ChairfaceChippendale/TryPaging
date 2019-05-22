package com.example.pagingsample.paging

import androidx.paging.DataSource
import com.example.pagingsample.database.EmployeeDao
import com.example.pagingsample.other.Employee


class DbItemKeyedDataSourceFactory(
    private val employeeDao: EmployeeDao
): DataSource.Factory<Long, Employee>() {

    override fun create(): DataSource<Long, Employee> {
        return DbItemKeyedDataSource(employeeDao)
    }
}