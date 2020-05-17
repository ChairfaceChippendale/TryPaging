package com.example.pagingsample.paging


import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.core.content.ContextCompat
import com.example.pagingsample.other.Employee
import kotlinx.android.synthetic.main.vh_employee.*
import java.text.SimpleDateFormat
import java.util.*

class EmployeeViewHolder(
    override val containerView: View
) : MessageAdapter.MessageViewHolder(containerView) {

    override fun bind(model: Employee?) {

        model?.let {
            tv_title.text = model.name
            tv_desc.text = formatDate(model.timeMilis)
        }


        selection.background = ColorDrawable(
            ContextCompat.getColor(
                containerView.context,
                android.R.color.transparent
            )
        )

    }

    private fun formatDate(time: Long): String {
        val dateTime = Calendar.getInstance()
        dateTime.timeInMillis = time

        val dateTimeFormatString = "MMMM dd yyyy"

        return SimpleDateFormat(dateTimeFormatString, Locale.US).format(dateTime.time)

    }
}