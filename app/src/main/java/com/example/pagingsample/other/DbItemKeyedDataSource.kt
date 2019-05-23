package com.example.pagingsample.other

import android.util.Log
import androidx.paging.ItemKeyedDataSource
import com.example.pagingsample.database.EmployeeDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

class DbItemKeyedDataSource(
    private val employeeDao: EmployeeDao
) : ItemKeyedDataSource<Employee, Employee>() {

    private val disposable = CompositeDisposable()

    override fun loadInitial(params: LoadInitialParams<Employee>, callback: LoadInitialCallback<Employee>) {
        disposable.addAll(
            employeeDao.getInitial(params.requestedLoadSize)
                .map {list ->  list.map { it.toEmployee() }.asReversed() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = {
                        Log.w("MYTAG", "loadInitial: ${it.size}")
                        callback.onResult(it.withDateHeaders(null))
                    },
                    onError = {
                        it.printStackTrace()
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Employee>, callback: LoadCallback<Employee>) {
        disposable.add(
            employeeDao.getAfter(params.requestedLoadSize, params.key.timeMilis)
                .map {list ->  list.map { it.toEmployee() }.asReversed() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = {
                        Log.w("MYTAG", "loadAfter: ${it.size}")
                        callback.onResult(it.withDateHeaders(null))
                    },
                    onError = {
                        it.printStackTrace()
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Employee>, callback: LoadCallback<Employee>) {
        val beforeDateMilis = params.key
        disposable.add(
            employeeDao.getBefore(params.requestedLoadSize, params.key.timeMilis)
                .map {list ->  list.map { it.toEmployee() }.asReversed() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = {
                        Log.w("MYTAG", "loadBefore: ${it.size}")
                        callback.onResult(it.withDateHeaders(params.key))
                    },
                    onError = {
                        it.printStackTrace()
                    }
                )
        )
    }

    override fun getKey(item: Employee): Employee = item

    fun clear() {
        disposable.dispose()
    }
}

fun List<Employee>.withDateHeaders(beforeEmployee: Employee?): List<Employee> {

    val datedList = ArrayList<Employee>()


    forEachIndexed { index, messageModel ->
        val next = this.getOrNull(index + 1) ?: beforeEmployee
        if (next == null){
            datedList.add(messageModel)
        } else {

            val thisCalendar = Calendar.getInstance().apply {
                timeInMillis = messageModel.timeMilis
            }

            val nextCalendar = Calendar.getInstance().apply {
                timeInMillis = next.timeMilis
            }

            if ((thisCalendar.get(Calendar.YEAR) == nextCalendar.get(Calendar.YEAR) &&
                        thisCalendar.get(Calendar.MONTH) == nextCalendar.get(Calendar.MONTH) &&
                        thisCalendar.get(Calendar.DAY_OF_MONTH) == nextCalendar.get(Calendar.DAY_OF_MONTH)) //check whether the same date day
            ) {
                datedList.add(messageModel)
            } else {
                datedList.add(messageModel)
                datedList.add(Employee.dateInst(next.timeMilis + 1)) //to make it less then next item (in case of sort)
            }
        }
    }

    return datedList

}