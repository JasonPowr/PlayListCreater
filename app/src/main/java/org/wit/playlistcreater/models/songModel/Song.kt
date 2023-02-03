package org.wit.playlistcreater.models.songModel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Song(
    val added_at: String,
    val added_by: AddedBy,
    val is_local: Boolean,
    val sharing_info: SharingInfo,
    val track: Track,
   // val video_thumbnail: VideoThumbnail,
) : Parcelable