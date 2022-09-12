package com.dongkeun.toyproject.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import com.dongkeun.toyproject.R
import com.dongkeun.toyproject.function.OnClickInterface
import com.dongkeun.toyproject.model.Diary
import com.google.android.material.imageview.ShapeableImageView
import java.text.SimpleDateFormat
import java.util.*

class DiaryListAdapter(private val list: List<Diary>, private val clickInterface: OnClickInterface) :
    RecyclerView.Adapter<DiaryListAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ListViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.diary_list_item, parent, false)
    )

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    inner class ListViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private val title: TextView = view.findViewById(R.id.diary_title)
        private val likeIcon: ImageView = view.findViewById(R.id.diary_like_icon)
        private val likeCount: TextView = view.findViewById(R.id.diary_like_count)
        private val commentCount: TextView = view.findViewById(R.id.diary_comment_count)
        private val updateTime: TextView = view.findViewById(R.id.diary_updateTime)

        fun bind(item: Diary) {
            title.text = item.title
//            thumbnail.setImageBitmap(item.thumbnail ?: getDefaultThumbnail(itemView.context))

            likeIcon.setColorFilter(
                if (item.isLiked) ContextCompat.getColor(itemView.context, R.color.light_violet)
                else ContextCompat.getColor(itemView.context, R.color.light_grey)
            )

            likeCount.text = item.likesCount.toString()
            commentCount.text = item.comments.size.toString()
            updateTime.text = makeLastUpdateTime(item)

            itemView.setOnClickListener { clickInterface.onClick(item.id) }
        }
    }

    private fun getDefaultThumbnail(context: Context) = ContextCompat.getDrawable(context, R.drawable.ic_default_photo)?.toBitmap()

    private fun makeLastUpdateTime(diary: Diary): String {
        val date = SimpleDateFormat("MMM dd, HH:mm:ss")
            .format(Date(diary.updatedTime ?: diary.createdTime))
        return "Last Updated: $date"
    }

}