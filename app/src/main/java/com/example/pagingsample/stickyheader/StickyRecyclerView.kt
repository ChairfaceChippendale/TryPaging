package com.example.pagingsample.stickyheader

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

class StickyRecyclerView: RecyclerView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attributes: AttributeSet?) : super(context, attributes)

    constructor(context: Context, attributes: AttributeSet?, defStyle: Int) : super(context, attributes, defStyle)

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
        if (adapter is StickyListener) {
            setStickyItemDecoration()
        }
    }

    private fun setStickyItemDecoration(){
        addItemDecoration(DateItemDecoration(adapter as StickyListener))
    }
}