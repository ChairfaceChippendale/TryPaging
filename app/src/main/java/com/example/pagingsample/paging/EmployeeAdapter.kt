package com.example.pagingsample.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.pagingsample.R
import com.example.pagingsample.database.EmployeeDbEntity

class EmployeeAdapter :
    PagedListAdapter<EmployeeDbEntity, EmployeeViewHolder>(EmployeeDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        return LayoutInflater
            .from(parent.context)
            .inflate(R.layout.vh_employee, parent, false)
            .let { EmployeeViewHolder(it) }
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class EmployeeDiffUtilCallback : DiffUtil.ItemCallback<EmployeeDbEntity>() {

        override fun areItemsTheSame(oldItem: EmployeeDbEntity, newItem: EmployeeDbEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: EmployeeDbEntity, newItem: EmployeeDbEntity): Boolean {
            return oldItem.name == newItem.name
                    && oldItem.position == newItem.position
        }
    }
}