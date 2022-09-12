package com.dongkeun.toyproject.viewmodel

import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private var displayWidth = 0
    private var displayHeight = 0

    fun getDisplayWidth() = displayWidth

    fun setDisplayWidth(width: Int) {
        displayWidth = width
    }

    fun getDisplayHeight() = displayHeight

    fun setDisplayHeight(height: Int) {
        displayHeight = height
    }

}