package com.example.pagingsample.paging

import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pagingsample.R
import com.example.pagingsample.database.EmployeeDbEntity
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.vh_employee.*

class EmployeeViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(employee: EmployeeDbEntity?, isSelected: Boolean, click: (Int) -> Unit) {
        employee?.let {
            tv_title.setText(employee.name)
            tv_desc.setText(employee.position)

            root.setOnClickListener{click(employee.id)}
        }
        selection.background = ColorDrawable(
            ContextCompat.getColor(
                containerView.context,
                if (isSelected) R.color.semitrans_light_blue
                else android.R.color.transparent
            )
        )


    }
}