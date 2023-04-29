package org.wit.playlistcreater.models.songModel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArtistX(
    val external_urls: ExternalUrls? = null,
    val id: String? = null,
    val name: String? = null,
    val type: String? = null,
    val uri: String? = null,
) : Parcelable