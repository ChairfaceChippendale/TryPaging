package com.example.pagingsample.paging

import androidx.paging.ItemKeyedDataSource
import com.example.pagingsample.database.EmployeeDao
import com.example.pagingsample.other.Employee
import com.example.pagingsample.other.toEmployee
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class DbItemKeyedDataSource(private val employeeDao: EmployeeDao) : ItemKeyedDataSource<Long, Employee>() {

    private val disposable = CompositeDisposable()

    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Employee>) {
        disposable.add(
            employeeDao.getInitial(params.requestedLoadSize)
                .map {list ->  list.map { it.toEmployee() } }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = {
                        callback.onResult(it)
                    },
                    onError = {
                        it.printStackTrace()
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Employee>) {
        disposable.add(
            employeeDao.getAfter(params.requestedLoadSize, params.key)
                .map {list ->  list.map { it.toEmployee() } }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = {
                        callback.onResult(it)
                    },
                    onError = {
                        it.printStackTrace()
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Employee>) {
        disposable.add(
            employeeDao.getBefore(params.requestedLoadSize, params.key)
                .map {list ->  list.map { it.toEmployee() } }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = {
                        callback.onResult(it)
                    },
                    onError = {
                        it.printStackTrace()
                    }
                )
        )
    }

    override fun getKey(item: Employee): Long = item.timeMilis

    fun clear() {
        disposable.dispose()
    }
}