package com.example.pagingsample.paging

import androidx.paging.PositionalDataSource
import com.example.pagingsample.other.Employee
import com.example.pagingsample.other.EmployeeStorage

class MyPositionalDataSource(private val employeeStorage: EmployeeStorage): PositionalDataSource<Employee>() {

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Employee>) {
        val result: List<Employee> = employeeStorage.getData(params.requestedStartPosition, params.requestedLoadSize)
        callback.onResult(result, 0)
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Employee>) {
        val result: List<Employee> = employeeStorage.getData(params.startPosition, params.loadSize)
        callback.onResult(result)
    }
}