package org.wit.playlistcreater.models.songModel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SharingInfo(
    val share_id: String? = null,
    val share_url: String? = null,
    val uri: String? = null,
) : Parcelable