package com.example.pagingsample.paging

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pagingsample.R
import com.example.pagingsample.other.Employee
import com.example.pagingsample.stickyheader.StickyListener
import kotlinx.android.extensions.LayoutContainer
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EmployeeAdapter :
    PagedListAdapter<Employee, EmployeeAdapter.MessageViewHolder>(EmployeeDiffUtilCallback()), StickyListener {

    private val selectedItemsIds: MutableList<String> by lazy { ArrayList<String>() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return when (viewType) {
            ViewType.MESSAGE_EMPLOYEE.ordinal -> MessageViewHolder.instEmpl(parent)
            ViewType.MESSAGE_DATE.ordinal -> MessageViewHolder.instDate(parent)
            else -> throw IllegalArgumentException("Wrong ViewType Of Message: $viewType")
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(
            model = getItem(position),
            isSelected = getItem(position)?.id in selectedItemsIds,
            click = { id ->
                val isRemoved = selectedItemsIds.removeAll { it == id }
                if (!isRemoved) selectedItemsIds.add(id)
                notifyItemChanged(position)
            }
        )
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position)?.let {
            ViewType.getType(it).ordinal
        } ?: -1
    }


    //sticky header
    override fun getHeaderPositionForItem(position: Int): Int {
        var headerPosition = 0
        for (i in position downTo 1) {
            if (isHeader(i)) {
                headerPosition = i
                return headerPosition
            }
        }
        return headerPosition
    }

    override fun getHeaderLayout(position: Int): Int = R.layout.vh_date

    override fun bindHeaderData(header: View, position: Int) {
        val tv = header.findViewById<TextView>(R.id.tvDate)
        getItem(position)?.timeMilis?.let {
            tv.text = formatDate(it, tv.resources)
        }
    }

    override fun isHeader(position: Int): Boolean {
        return getItem(position)?.type == Employee.Type.MESSAGE_DATE
    }


    //diffutils
    private class EmployeeDiffUtilCallback : DiffUtil.ItemCallback<Employee>() {

        override fun areItemsTheSame(oldItem: Employee, newItem: Employee): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Employee, newItem: Employee): Boolean {
            return oldItem.name == newItem.name
                    && oldItem.timeMilis == newItem.timeMilis
                    && oldItem.type == newItem.type
        }
    }


    abstract class MessageViewHolder(
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        companion object {
            fun instEmpl(
                parent: ViewGroup
            ): MessageViewHolder =
                EmployeeViewHolder(parent.inflate(R.layout.vh_employee))

            fun instDate(
                parent: ViewGroup
            ): MessageViewHolder =
                DateBubbleViewHolder(parent.inflate(R.layout.vh_date))
        }

        abstract fun bind(model: Employee?, isSelected: Boolean, click: (String) -> Unit)

    }

    enum class ViewType {
        MESSAGE_EMPLOYEE,
        MESSAGE_DATE;

        companion object {
            fun getType(employee: Employee): ViewType {
                return when (employee.type) {
                    Employee.Type.MESSAGE_DATE -> MESSAGE_DATE
                    Employee.Type.MESSAGE_EMPLOYEE -> MESSAGE_EMPLOYEE
                }
            }
        }
    }

    private fun formatDate(time: Long, res: Resources): String {
        val dateTime = Calendar.getInstance()
        dateTime.timeInMillis = time

        val dateTimeFormatString = "MMMM dd yyyy"

        return SimpleDateFormat(dateTimeFormatString, Locale.US).format(dateTime.time)

    }


}

fun ViewGroup.inflate(@LayoutRes layoutId: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context)
        .inflate(layoutId, this, attachToRoot)