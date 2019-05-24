package com.example.pagingsample.other

import com.example.pagingsample.database.EmployeeDbEntity
import java.util.*

class Employee(
    val id: String,
    val name: String,
    val timeMilis: Long,
    val type: Type = Type.MESSAGE_EMPLOYEE
) {

    companion object {
        fun dateInst(timeMilis: Long) =
            Employee(
                id = "DateBubble${timeMilis}",
                name = "This is just date bubble",
                timeMilis = timeMilis,
                type = Type.MESSAGE_DATE
            )
    }

    enum class Type{
        MESSAGE_EMPLOYEE,
        MESSAGE_DATE
    }
}

fun EmployeeDbEntity.toEmployee(): Employee {
    return Employee(
        id = id,
        name = name,
        timeMilis = date,
        type = Employee.Type.MESSAGE_EMPLOYEE
    )
}