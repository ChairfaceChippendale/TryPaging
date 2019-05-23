package com.example.pagingsample.paging

import android.annotation.SuppressLint
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pagingsample.App
import com.example.pagingsample.R
import com.example.pagingsample.other.Employee
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val adapter: EmployeeAdapter =
        EmployeeAdapter()

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_main.layoutManager = LinearLayoutManager(this)

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
                onNext = {
                    adapter.submitList(it)
                    Log.w("MYTAG", "${it.size}")
                },
                onError = { it.printStackTrace() }
            )

        rv_main.adapter = adapter

        scroll.setOnClickListener {
            Log.w("MYTAG", "${adapter.itemCount}")
            rv_main.scrollToPosition(adapter.itemCount-1)//bottom

        }
    }
}
