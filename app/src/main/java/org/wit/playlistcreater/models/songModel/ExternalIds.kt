package org.wit.playlistcreater.models.songModel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExternalIds(
    val isrc: String? = null,
) : Parcelable