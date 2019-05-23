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
            .setBoundaryCallback(object :PagedList.BoundaryCallback<Employee>(){
                override fun onZeroItemsLoaded() {
                    super.onZeroItemsLoaded()
                    Log.w("MYTAG", "onZeroItemsLoaded")
                }

                override fun onItemAtEndLoaded(itemAtEnd: Employee) {
                    super.onItemAtEndLoaded(itemAtEnd)
                    Log.w("MYTAG", "onItemAtEndLoaded")
                }

                override fun onItemAtFrontLoaded(itemAtFront: Employee) {
                    super.onItemAtFrontLoaded(itemAtFront)
                    Log.w("MYTAG", "onItemAtFrontLoaded")
                }
            })
            .buildFlowable(BackpressureStrategy.BUFFER)
            .subscribeBy(
                onNext = {

                    it.addWeakCallback(null, object: PagedList.Callback(){
                        override fun onChanged(position: Int, count: Int) {
                            Log.w("MYTAG", "onChanged pos: $position, count: $count")
                        }

                        override fun onInserted(position: Int, count: Int) {
                            Log.w("MYTAG", "onInserted pos: $position, count: $count")
                            rv_main.scrollToBottom()

                        }

                        override fun onRemoved(position: Int, count: Int) {
                            Log.w("MYTAG", "onRemoved pos: $position, count: $count")
                        }
                    })
                    adapter.submitList(it) },
                onError = { it.printStackTrace() }
            )


        rv_main.layoutManager = LinearLayoutManager(this)
        rv_main.adapter = adapter




        scroll.setOnClickListener {
            Log.w("MYTAG", "${adapter.itemCount}")
            rv_main.scrollToPosition(adapter.itemCount-1)//bottom

        }
    }

}


fun RecyclerView.scrollToBottom() {
    adapter?.let { notNullAdapter ->
        val lastItemPosition = notNullAdapter.itemCount - 1
        scrollToPosition(lastItemPosition)
    }
}
