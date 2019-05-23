package com.example.pagingsample.paging

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pagingsample.App
import com.example.pagingsample.R
import com.example.pagingsample.other.Employee
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val adapter: EmployeeAdapter =
        EmployeeAdapter()

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val config: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(10)
            .build()

        val factory = DbItemKeyedDataSourceFactory(App.instance.database.employeeDao())

        RxPagedListBuilder<Employee, Employee>(factory, config)
            .setFetchScheduler(Schedulers.newThread())
            .setNotifyScheduler(AndroidSchedulers.mainThread())
            .buildFlowable(BackpressureStrategy.BUFFER)
            .subscribeBy(
                onNext = { adapter.submitList(it) },
                onError = { it.printStackTrace() }
            )


        rv_main.layoutManager = LinearLayoutManager(this)
        rv_main.adapter = adapter
//        rv_main.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
//            if (bottom < oldBottom && isBottomOfList()) {
//                rv_main.post { rv_main.scrollToBottom() }
//            }
//        }


        scroll.setOnClickListener {
            Log.w("MYTAG", "${adapter.itemCount}")
            rv_main.scrollToPosition(adapter.itemCount-1)//bottom

        }
    }

    private fun isBottomOfList(): Boolean {
        val lastVisiblePosition = (rv_main.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        val lastItemPosition = (rv_main.adapter?.itemCount ?: 0) - 1

        return (lastItemPosition - lastVisiblePosition) < 10
    }
}


fun RecyclerView.scrollToBottom() {
    adapter?.let { notNullAdapter ->
        val lastItemPosition = notNullAdapter.itemCount - 1
        scrollToPosition(lastItemPosition)
    }
}
