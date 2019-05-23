package com.example.pagingsample.other

import android.util.Log
import androidx.paging.DataSource
import com.example.pagingsample.App
import com.example.pagingsample.database.EmployeeDao
import com.example.pagingsample.other.DbItemKeyedDataSource
import com.example.pagingsample.other.Employee
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers


class DbItemKeyedDataSourceFactory(
    private val employeeDao: EmployeeDao
): DataSource.Factory<Employee, Employee>() {


    override fun create(): DataSource<Employee, Employee> {
        Log.w("MYTAG", "create new ItemKeyedDataSource")

        val d = DbItemKeyedDataSource(employeeDao)


        return d

    }
}