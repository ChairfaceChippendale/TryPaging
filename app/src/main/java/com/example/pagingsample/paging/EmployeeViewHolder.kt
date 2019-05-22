package com.example.pagingsample.paging

import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pagingsample.R
import com.example.pagingsample.database.EmployeeDbEntity
import com.example.pagingsample.other.Employee
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.vh_employee.*

class EmployeeViewHolder(
    override val containerView: View
) : EmployeeAdapter.MessageViewHolder(containerView) {

    override fun bind(model: Employee?, isSelected: Boolean, click: (String) -> Unit) {

        model?.let {
            tv_title.setText(model.name)
            tv_desc.setText(model.position)

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
}