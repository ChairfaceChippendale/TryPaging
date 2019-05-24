package com.example.pagingsample.paging

import android.content.res.Resources
import android.view.View
import com.example.pagingsample.other.Employee
import kotlinx.android.synthetic.main.vh_date.*
import java.text.SimpleDateFormat
import java.util.*


class DateBubbleViewHolder(
    container: View
) : MessageAdapter.MessageViewHolder(container) {

    override fun bind(model: Employee?, isSelected: Boolean, click: (String) -> Unit) {
        model?.let{
            tvDate.text = formatDate(model.timeMilis)
        }
    }




    fun formatDate(time: Long): String {
        val dateTime = Calendar.getInstance()
        dateTime.timeInMillis = time

        val dateTimeFormatString = "dd MMMM yyyy"

        return SimpleDateFormat(dateTimeFormatString, Locale.US).format(dateTime.time)

    }
}