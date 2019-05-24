package com.example.pagingsample.paging

import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pagingsample.R
import com.example.pagingsample.other.Employee
import com.example.pagingsample.stickyheader.StickyListener
import kotlinx.android.extensions.LayoutContainer
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MessageAdapter :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>(), StickyListener {

    private val items: MutableList<Employee> = mutableListOf()

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
            model = items[position],
            isSelected = items[position].id in selectedItemsIds,
            click = { id ->
                val isRemoved = selectedItemsIds.removeAll { it == id }
                if (!isRemoved) selectedItemsIds.add(id)
                notifyItemChanged(position)
            }
        )
    }

    override fun getItemViewType(position: Int): Int = ViewType.getType(items[position]).ordinal

    override fun getItemCount(): Int = items.size


    fun addItems(items: List<Employee>) {
        val newItems = ArrayList(this.items).apply { addAll(items) }
        val diffResult = DiffUtil.calculateDiff(DiffUtilCallback(oldList = this.items, newList = newItems))
        this.items.clear()
        this.items.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    fun setItems(items: List<Employee>) {
        val newItems = ArrayList(items)
        val diffResult = DiffUtil.calculateDiff(DiffUtilCallback(oldList = this.items, newList = newItems))
        this.items.clear()
        this.items.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    //====== STICKIE ======

    override fun getHeaderPositionForItem(position: Int): Int {
        var headerPosition = 0
        for (i in position until itemCount) {
//        for (i in position downTo 1) {
            if (isHeader(i)) {
                headerPosition = i
//                Log.w("MYTAG", " headerPosition: $position")
                return headerPosition
            }
        }
        return headerPosition
    }

    override fun getHeaderLayout(position: Int): Int = R.layout.vh_date

    override fun bindHeaderData(header: View, position: Int) {
//        Log.w("MYTAG", " bindHeaderData: $position")
        val tv = header.findViewById<TextView>(R.id.tvDate)
        tv.text = formatDate(items[position].timeMilis)
    }

    override fun isHeader(position: Int): Boolean =
        items[position].type == Employee.Type.MESSAGE_DATE



    //====== DIFFUTILS ======

    private inner class DiffUtilCallback(
        val oldList: List<Employee>,
        val newList: List<Employee>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(firstPos: Int, secondPos: Int) =
            oldList[firstPos].id == newList[secondPos].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            return oldItem.name == newItem.name
                    && oldItem.timeMilis == newItem.timeMilis
                    && oldItem.type == newItem.type
        }
    }


    //========= VH ============

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

    // ========== OTHER ============

    private fun formatDate(time: Long): String {
        val dateTime = Calendar.getInstance()
        dateTime.timeInMillis = time

        val dateTimeFormatString = "dd MMMM yyyy"

        return SimpleDateFormat(dateTimeFormatString, Locale.US).format(dateTime.time)

    }


}

fun ViewGroup.inflate(@LayoutRes layoutId: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context)
        .inflate(layoutId, this, attachToRoot)