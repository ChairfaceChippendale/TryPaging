package com.example.pagingsample.paging

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pagingsample.App
import com.example.pagingsample.R
import com.example.pagingsample.database.EmployeeDbEntity
import io.reactivex.BackpressureStrategy
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private val adapter: EmployeeAdapter =
        EmployeeAdapter()

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_main.layoutManager = LinearLayoutManager(this)

        val dataSource =
            App.instance.database.employeeDao().getAll()

        val config: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(10)
            .build()

        RxPagedListBuilder<Int, EmployeeDbEntity>(dataSource, config)
            .setFetchScheduler(Schedulers.newThread())
            .setNotifyScheduler(AndroidSchedulers.mainThread())
            .buildFlowable(BackpressureStrategy.BUFFER)
            .subscribeBy(onNext = {
                adapter.submitList(it)
            })

        rv_main.adapter = adapter
    }
}
