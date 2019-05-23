package com.example.pagingsample.paging

import android.content.res.Resources
import android.view.View
import com.example.pagingsample.other.Employee
import kotlinx.android.synthetic.main.vh_date.*
import java.text.SimpleDateFormat
import java.util.*

class DateBubbleViewHolder (
    container: View
) : EmployeeAdapter.MessageViewHolder(container) {

    override fun bind(model: Employee?, isSelected: Boolean, click: (String) -> Unit) {
        model?.let{
            tvDate.text = formatDate(model.timeMilis, tvDate.resources)
        }
    }

    fun formatDate(time: Long, res: Resources): String {
        val dateTime = Calendar.getInstance()
        dateTime.timeInMillis = time

        val dateTimeFormatString = "MMMM dd yyyy"

        return SimpleDateFormat(dateTimeFormatString, Locale.US).format(dateTime.time)

    }
}