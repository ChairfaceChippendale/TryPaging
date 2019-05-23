package com.example.pagingsample.paging

import androidx.paging.ItemKeyedDataSource
import com.example.pagingsample.database.EmployeeDao
import com.example.pagingsample.other.Employee
import com.example.pagingsample.other.toEmployee
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

class DbItemKeyedDataSource(private val employeeDao: EmployeeDao) : ItemKeyedDataSource<Employee, Employee>() {

    private val disposable = CompositeDisposable()

    override fun loadInitial(params: LoadInitialParams<Employee>, callback: LoadInitialCallback<Employee>) {
        disposable.add(
            employeeDao.getInitial(params.requestedLoadSize)
                .map {list ->  list.map { it.toEmployee() } }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = {
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
                .map {list ->  list.map { it.toEmployee() } }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = {
                        callback.onResult(it.withDateHeaders(params.key))
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
                .map {list ->  list.map { it.toEmployee() } }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = {
                        callback.onResult(it.withDateHeaders(null))
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

fun List<Employee>.withDateHeaders(afterEmployee: Employee?): List<Employee> {

    val datedList = ArrayList<Employee>()


    forEachIndexed { index, messageModel ->
        val previous = this.getOrNull(index - 1) ?: afterEmployee
        if (previous == null){
            datedList.add(Employee.dateInst(messageModel.timeMilis - 1)) //to make it less then next item (in case of sort)
            datedList.add(messageModel)
        } else {

            val thisCalendar = Calendar.getInstance().apply {
                timeInMillis = messageModel.timeMilis
            }

            val previousCalendar = Calendar.getInstance().apply {
                timeInMillis = previous.timeMilis
            }

            if ((thisCalendar.get(Calendar.YEAR) == previousCalendar.get(Calendar.YEAR) &&
                        thisCalendar.get(Calendar.MONTH) == previousCalendar.get(Calendar.MONTH) &&
                        thisCalendar.get(Calendar.DAY_OF_MONTH) == previousCalendar.get(Calendar.DAY_OF_MONTH)) //check whether the same date day
            ) {
                datedList.add(messageModel)
            } else {
                datedList.add(Employee.dateInst(messageModel.timeMilis)) //to make it less then next item (in case of sort)
                datedList.add(messageModel)
            }
        }
    }

    return datedList

}