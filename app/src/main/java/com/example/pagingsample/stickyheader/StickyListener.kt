package com.example.pagingsample.stickyheader

import android.view.View

interface StickyListener {

    fun getHeaderPositionForItem(position: Int): Int

    fun getHeaderLayout(position: Int): Int

    fun bindHeaderData(header: View, position: Int)

    fun isHeader(position: Int): Boolean
}