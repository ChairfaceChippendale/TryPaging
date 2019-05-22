package com.example.pagingsample.other

import java.util.*


class Employee(
    val id: String,
    val name: String,
    val position: String,
    val timeMilis: Long,
    val type: Type = Type.MESSAGE_EMPLOYEE
) {

    companion object {
        fun dateInst(timeMilis: Long) =
            Employee(
                id = UUID.randomUUID().toString(),
                name = "This is just date bubble",
                position = "",
                timeMilis = timeMilis,
                type = Type.MESSAGE_DATE
            )

    }

    enum class Type{
        MESSAGE_EMPLOYEE,
        MESSAGE_DATE
    }
}