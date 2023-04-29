package org.wit.playlistcreater.models.songModel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Songs(
    val added_at: String? = null,
    val added_by: AddedBy? = null,
    val is_local: Boolean? = null,
    val sharing_info: SharingInfo? = null,
    val track: Track? = null,
    var isInPlaylist: Boolean? = null,
    //val video_thumbnail: VideoThumbnail,
) : Parcelable