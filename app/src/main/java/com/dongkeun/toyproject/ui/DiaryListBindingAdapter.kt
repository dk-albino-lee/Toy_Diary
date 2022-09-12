package com.dongkeun.toyproject.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import com.dongkeun.toyproject.R
import com.dongkeun.toyproject.databinding.DiaryListItemBinding
import com.dongkeun.toyproject.function.OnClickInterface
import com.dongkeun.toyproject.function.StaticValue
import com.dongkeun.toyproject.model.Diary
import java.lang.Exception

class DiaryListBindingAdapter(private val clickInterface: OnClickInterface) :
    RecyclerView.Adapter<DiaryListBindingAdapter.DiaryViewHolder>() {

    var list = listOf<Diary>()

    inner class DiaryViewHolder(private val binding: DiaryListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Diary) {

            val params = itemView.layoutParams
            params.height = (StaticValue.getHeight() * 0.3).toInt()
            itemView.layoutParams = params

            with(binding){
                diary = item
                click = clickInterface

                diaryLikeIcon.setColorFilter(
                    if (item.isLiked) ContextCompat.getColor(itemView.context, R.color.light_violet)
                    else ContextCompat.getColor(itemView.context, R.color.light_grey)
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        val binding = DiaryListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return DiaryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    fun makeList(newList: List<Diary>) {
        list = newList
    }

    private fun getDefaultThumbnail(context: Context) = ContextCompat.getDrawable(context, R.drawable.ic_default_photo)?.toBitmap()

    fun toBitmap(encodedString: String): Bitmap? {
        try {
            val encodeByte = Base64.decode(encodedString, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        } catch (e: Exception) {
            return null
        }
    }
}