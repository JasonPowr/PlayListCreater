package org.wit.playlistcreater.models.songModel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Image(
    val height: Int? = null,
    val url: String? = null,
    val width: Int? = null,
) : Parcelable