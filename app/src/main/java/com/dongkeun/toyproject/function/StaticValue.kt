package com.dongkeun.toyproject.function

class StaticValue {

    companion object {
        var widthPixel: Int = 0
        var heightPixel: Int = 0

        fun getWidth() = widthPixel

        fun setWidth(width: Int) {
            widthPixel = width
        }

        fun getHeight() = heightPixel

        fun setHeight(height: Int) {
            heightPixel = height
        }
    }

}