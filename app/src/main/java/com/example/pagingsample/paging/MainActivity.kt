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

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val dao = App.instance.database.employeeDao()


        val lm = LinearLayoutManager(this)
        lm.reverseLayout = true
        rv_main.addOnScrollListener(object : RecyclerView.OnScrollListener(){

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                // 3 lines below are not needed.
                Log.w("MYTAG","Last visible item is: ${lm.findLastVisibleItemPosition()}")
//                Log.w("MYTAG","Item count is: ${lm.itemCount}")
//                Log.w("MYTAG","end? : ${lm.findLastVisibleItemPosition() == lm.itemCount - 1}")

                if (lm.itemCount - lm.findLastVisibleItemPosition() < 5){
                    Log.e("MYTAG","time to lode more : ${lm.itemCount - lm.findLastVisibleItemPosition()}")
                }

                if(lm.findLastVisibleItemPosition() == lm.itemCount - 1){
                    // We have reached the end of the recycler view.
                }
                super.onScrolled(recyclerView, dx, dy)
            }


        })
        rv_main.layoutManager = lm
        rv_main.adapter = adapter





        itemSubscriber = dao.getLimit(50)
            .map { list -> list.map { it.toEmployee() }.withDateHeaders() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
//                    Log.w("MYTAG", "Initial load")
                    adapter.setItems(it)
                },
                onError = {
                    it.printStackTrace()
                }
            )


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

