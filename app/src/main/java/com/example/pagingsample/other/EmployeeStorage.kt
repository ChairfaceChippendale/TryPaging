package com.example.pagingsample.other

class EmployeeStorage {

    fun getData(requestedStartPosition: Int, requestedLoadSize: Int): List<Employee> {
        val result = mutableListOf<Employee>()

        for(i in requestedStartPosition until requestedStartPosition + requestedLoadSize) {
            result += Employee(i.toString(), "EmployeeEntity #$i", "Position #$i")
        }

        return result
    }

}