package com.example.pagingsample.database

import androidx.paging.DataSource
import androidx.room.Embedded
import androidx.room.Relation

class CompanyWithEmployeesDbEntity (

    @Embedded val chatsDbEntity: CompanyDbEntity

    // Didn't work Fields annotated with @Relation must be a List or Set.
//    @Relation(
//        parentColumn = "id",
//        entityColumn = "company_id"
//    ) val employeesEntities: DataSource.Factory<Int, EmployeeDbEntity>

)