package org.wit.playlistcreater.models.songModel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LinkedFrom(
    val external_urls: ExternalUrls,
    val id: String,
    val type: String,
    val uri: String
) : Parcelable