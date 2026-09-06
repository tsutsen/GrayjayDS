package com.tsutsen.platformplayer.core.database.entity

import androidx.room.Entity
import com.tsutsen.platformplayer.core.model.SavedVideoType

@Entity(
    tableName = "saved_video",
    primaryKeys = ["contentUrl", "type"],
)
data class SavedVideoEntity(
    val contentUrl: String,
    val type: SavedVideoType,
    val title: String,
    val author: String?,
    val authorUrl: String? = null,
    val thumbnailUrl: String?,
    val durationMs: Long = 0,
    val viewCount: Long = 0,
    val addedAt: Long = System.currentTimeMillis(),
    /**
     * The video's real posted date, captured at save time (0 = unknown for
     * pre-migration rows). Shown in the card's bottom-left pill; distinct
     * from [addedAt] (when it was saved to this section).
     */
    val postedAt: Long = 0,
)
