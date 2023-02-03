package org.wit.playlistcreater.models.songModel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SharingInfo(
    val share_id: String,
    val share_url: String,
    val uri: String
) : Parcelable