package com.example.pagingsample

import android.annotation.SuppressLint
import android.app.Application
import androidx.room.Room
import com.example.pagingsample.database.AppDatabase
import com.example.pagingsample.database.CompanyDbEntity
import com.example.pagingsample.database.EmployeeDbEntity
import com.facebook.stetho.Stetho
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.*

class App : Application() {

    lateinit var database: AppDatabase
        private set

    @SuppressLint("CheckResult")
    override fun onCreate() {
        super.onCreate()
        instance = this
        database = Room.databaseBuilder(this, AppDatabase::class.java, "database")
            .build()

        Stetho.initializeWithDefaults(this);

        val employees = mutableListOf<EmployeeDbEntity>()

        val r = Random()


        for (i in 0..100) {
            employees += EmployeeDbEntity(i.toString(), "Name $i", 1514757600045 + (8_000_000 * i))
        }

        Completable.fromAction {
            database.employeeDao().insertAll(employees)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {}
            )
    }

    companion object {
        lateinit var instance: App
            private set
    }

}