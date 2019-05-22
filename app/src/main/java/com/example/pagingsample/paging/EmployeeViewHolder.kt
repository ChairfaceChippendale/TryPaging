package com.example.pagingsample.paging

import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pagingsample.R
import com.example.pagingsample.database.EmployeeDbEntity
import com.example.pagingsample.other.Employee
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.vh_employee.*
import java.text.SimpleDateFormat
import java.util.*

class EmployeeViewHolder(
    override val containerView: View
) : EmployeeAdapter.MessageViewHolder(containerView) {

    override fun bind(model: Employee?, isSelected: Boolean, click: (String) -> Unit) {

        model?.let {
            tv_title.text = model.name
            tv_desc.text = formatDate(model.timeMilis, tv_desc.resources)

            root.setOnClickListener { click(model.id) }
        }


        selection.background = ColorDrawable(
            ContextCompat.getColor(
                containerView.context,
                if (isSelected) R.color.semitrans_light_blue
                else android.R.color.transparent
            )
        )

    }

    fun formatDate(time: Long, res: Resources): String {
        val dateTime = Calendar.getInstance()
        dateTime.timeInMillis = time

        val dateTimeFormatString = "MMMM dd yyyy"

        return SimpleDateFormat(dateTimeFormatString, Locale.US).format(dateTime.time)

    }
}