package com.dongkeun.toyproject.model

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

data class Diary(
    val id: String = "",
    val account: String = "",
    val title: String = "",
    val content: String = "",
    val imageUri: String? = null,
    val likesCount: Int = 0,
    val isLiked: Boolean = false,
    val comments: List<String> = listOf(),
    val createdTime: Long = 0L,
    val updatedTime: Long? = null,
    val isThumbnailExist: Boolean = false
) {
    companion object {
        fun TEST() = Diary(
            "",
            account = "",
            "title",
            "content",
            null,
            58,
            true,
            listOf("Haha"),
            20220808203800,
            null)

        fun EMPTY() = Diary(
            id = "",
            account = "",
            title = "Title",
            content = "Content",
            imageUri = null,
            likesCount = 0,
            isLiked = false,
            comments = emptyList(),
            createdTime = 19000101000000,
            updatedTime = null,
            isThumbnailExist = false
        )
    }

    fun mappingForFireStore(): HashMap<String, Any?> {
        return hashMapOf(
            "id" to id,
            "account" to account,
            "title" to title,
            "content" to content,
            "imageUri" to imageUri,
            "likes_count" to likesCount,
            "is_liked" to isLiked,
            "comments" to comments,
            "created_time" to createdTime,
            "updated_time" to updatedTime,
            "is_thumbnail_exist" to isThumbnailExist
        )
    }

    fun makeLastUpdateTime(): String {
        val date = SimpleDateFormat("MMM dd, HH:mm:ss")
            .format(Date(this.updatedTime ?: this.createdTime))
        return "Last Updated: $date"
    }
}
