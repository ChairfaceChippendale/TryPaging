package com.example.pagingsample.paging

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pagingsample.App
import com.example.pagingsample.R
import com.example.pagingsample.database.EmployeeDbEntity
import com.example.pagingsample.other.Employee
import com.example.pagingsample.other.toEmployee
import io.reactivex.BackpressureStrategy
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val adapter: EmployeeAdapter = EmployeeAdapter()

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val dataSource =
            App.instance.database.employeeDao().getAll().mapByPage {list -> list.map { it.toEmployee() } }

        val config: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setMaxSize(100)
            .build()

        RxPagedListBuilder<Int, Employee>(dataSource, config)
            .setFetchScheduler(Schedulers.newThread())
            .setNotifyScheduler(AndroidSchedulers.mainThread())
            .buildFlowable(BackpressureStrategy.BUFFER)
            .subscribeBy(
                onNext = { adapter.submitList(it) },
                onError = { it.printStackTrace() }
            )

        val lm = LinearLayoutManager(this)
        lm.reverseLayout=true
        rv_main.layoutManager = lm
        rv_main.adapter = adapter

        btm_scroll.setOnClickListener {
            lm.scrollToPositionWithOffset(50, 0)
        }
    }
}
