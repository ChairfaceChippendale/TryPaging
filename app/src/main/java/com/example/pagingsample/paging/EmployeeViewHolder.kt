package com.example.pagingsample.paging

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.pagingsample.database.EmployeeDbEntity
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.vh_employee.*

class EmployeeViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(employee: EmployeeDbEntity?) {
        employee?.let {
            tv_title.setText(employee.name)
            tv_desc.setText(employee.position)
        }
    }
}