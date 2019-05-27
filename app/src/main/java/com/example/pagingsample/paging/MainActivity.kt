package com.example.pagingsample.paging

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pagingsample.App
import com.example.pagingsample.R
import com.example.pagingsample.database.EmployeeDbEntity
import com.example.pagingsample.other.Employee
import com.example.pagingsample.other.toEmployee
import com.jakewharton.rxbinding3.recyclerview.scrollEvents
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private val adapter: MessageAdapter = MessageAdapter()


    private var itemSubscriber: Disposable? = null
    val dao = App.instance.database.employeeDao()

    private var currentNum = 40


    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val lm = LinearLayoutManager(this)
        lm.reverseLayout = true

        rv_main.scrollEvents()
            .map { lm.findLastVisibleItemPosition() }
            .distinctUntilChanged()
            .map {
                Log.w("MYTAG","Last visible item is: $it")
//                Log.w("MYTAG","Item count is: ${lm.itemCount}")
//                Log.w("MYTAG","end? : ${lm.findLastVisibleItemPosition() == lm.itemCount - 1}")

                if (lm.itemCount - it == 3){
                    Log.e("MYTAG","time to load more : ${lm.itemCount - it}")
                    return@map true
                }
                return@map false
            }
            .subscribeBy(
                onNext = {
                    if (it) {
                        currentNum += 10
                        load(currentNum)
                    }
                },
                 onError = { it.printStackTrace() }
            )


        rv_main.layoutManager = lm
        rv_main.adapter = adapter



        load(currentNum)


        btn_update.setOnClickListener {
            dao.getById("45")
                .flatMapCompletable { dao.updateElement(it.copy(name = "updated")) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onError = { it.printStackTrace() },
                    onComplete = { Log.w("MYTAG", " updated") }
                )
        }

        btn_scroll.setOnClickListener {
//            Log.w("MYTAG", "${adapter.itemCount}")
            rv_main.scrollToPosition(0)//bottom

        }

        btn_load_more.setOnClickListener {

            itemSubscriber?.dispose()
            itemSubscriber = dao.getLimit(70)
                .map { list -> list.map { it.toEmployee() }.withDateHeaders() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = {
//                        Log.w("MYTAG", "Iter load")
                        adapter.setItems(it)
                    },
                    onError = {
                        it.printStackTrace()
                    }
                )
        }

        btn_add_more.setOnClickListener {

            Completable.fromAction {
                dao.insert(EmployeeDbEntity("2000", "Name last ", 1514757600045L + (9_000_000L * 300)))
            }
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                    onError = {}
                )
        }
    }


    private fun load(num: Int){
        itemSubscriber?.dispose()
        itemSubscriber =  dao.getLimit(num)
            .distinctUntilChanged()
            .map { list ->
                Log.w("MYTAG", "Load raw: ${list.size}")
                return@map list.map { it.toEmployee() }.withDateHeaders()
             }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    Log.w("MYTAG", "Load: ${it.size}")
                    adapter.setItems(it)
                },
                onError = {
                    it.printStackTrace()
                }
            )
    }

}


fun List<Employee>.withDateHeaders(): List<Employee> {

    val datedList = ArrayList<Employee>()

    forEachIndexed { index, messageModel ->
        val next = this.getOrNull(index + 1)
        if (next == null){
            datedList.add(messageModel)
            datedList.add(Employee.dateInst(messageModel.timeMilis + 1)) //to make it less then next item (in case of sort)
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
                datedList.add(Employee.dateInst(messageModel.timeMilis + 1)) //to make it less then next item (in case of sort)
            }
        }
    }

    return datedList

}

