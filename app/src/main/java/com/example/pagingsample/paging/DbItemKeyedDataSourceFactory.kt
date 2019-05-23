package com.example.pagingsample.paging

import androidx.paging.DataSource
import androidx.recyclerview.widget.RecyclerView
import com.example.pagingsample.database.EmployeeDao
import com.example.pagingsample.other.Employee


class DbItemKeyedDataSourceFactory(
    private val employeeDao: EmployeeDao
): DataSource.Factory<Employee, Employee>() {

    override fun create(): DataSource<Employee, Employee> {
        return DbItemKeyedDataSource(employeeDao)
    }
}