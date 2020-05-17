package com.example.pagingsample.paging


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pagingsample.R
import com.example.pagingsample.other.Employee
import kotlinx.android.extensions.LayoutContainer
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MessageAdapter :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private val items: MutableList<Employee> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder.instEmpl(parent)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(
            model = items[position]
        )
    }


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
        }

        abstract fun bind(model: Employee?)

    }



}

fun ViewGroup.inflate(@LayoutRes layoutId: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context)
        .inflate(layoutId, this, attachToRoot)