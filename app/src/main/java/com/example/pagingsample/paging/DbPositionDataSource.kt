package com.example.pagingsample.paging

import androidx.paging.ItemKeyedDataSource
import com.example.pagingsample.database.EmployeeDao
import com.example.pagingsample.other.Employee
import com.example.pagingsample.other.toEmployee
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class DbPositionDataSource(private val employeeDao: EmployeeDao) : ItemKeyedDataSource<Long, Employee>() {

    private val disposable = CompositeDisposable()

    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Employee>) {
        disposable.add(
            employeeDao.getFirst(params.requestedLoadSize)
                .map { it.map { it.toEmployee() } }
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Employee>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getKey(item: Employee): Long = item.timeMilis

    override fun invalidate() {
        super.invalidate()
    }


    fun clear() {
        disposable.dispose()
    }
}