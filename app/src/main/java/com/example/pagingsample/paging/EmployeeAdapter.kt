package com.example.pagingsample.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.pagingsample.R
import com.example.pagingsample.database.EmployeeDbEntity

class EmployeeAdapter :
    PagedListAdapter<EmployeeDbEntity, EmployeeViewHolder>(EmployeeDiffUtilCallback()) {

    val selectedItemsIds: MutableList<Int> by lazy { ArrayList<Int>() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        return LayoutInflater
            .from(parent.context)
            .inflate(R.layout.vh_employee, parent, false)
            .let { EmployeeViewHolder(it) }
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        holder.bind(getItem(position), getItem(position)?.id in selectedItemsIds ){id ->
            val isRemoved = selectedItemsIds.removeAll { it == id }
            if (!isRemoved) selectedItemsIds.add(id)
            notifyItemChanged(position)
        }
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