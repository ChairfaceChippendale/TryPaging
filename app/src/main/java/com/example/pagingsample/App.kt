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
        val companies = mutableListOf<CompanyDbEntity>()

        val r = Random()

        companies += CompanyDbEntity(0, "First company")
        companies += CompanyDbEntity(1, "Second company")
        companies += CompanyDbEntity(2, "Third company")

        Completable.fromAction {
            database.companyDao().insertAll(companies)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {}
            )

        for (i in 0..100) {
            employees += EmployeeDbEntity(i.toString(), "Name $i", "Position $i", r.nextInt(3), 1514757600000 + 86_400_005 * i)
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