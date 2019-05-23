package com.example.pagingsample.paging

import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pagingsample.R
import com.example.pagingsample.database.EmployeeDbEntity
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.vh_employee.*
import java.text.SimpleDateFormat
import java.util.*

class EmployeeViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(employee: EmployeeDbEntity?, isSelected: Boolean, click: (Int) -> Unit) {
        employee?.let {
            tv_title.setText(employee.name)
            tv_desc.text  = formatDate(employee.timeMilis)

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

    fun formatDate(time: Long): String {
        val dateTime = Calendar.getInstance()
        dateTime.timeInMillis = time

        val dateTimeFormatString = "MMMM dd yyyy"

        return SimpleDateFormat(dateTimeFormatString, Locale.US).format(dateTime.time)

    }
}