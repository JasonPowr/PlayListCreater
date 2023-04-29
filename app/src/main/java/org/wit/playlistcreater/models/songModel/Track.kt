package org.wit.playlistcreater.models.songModel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
    val album: Album? = null,
    val artists: List<ArtistX>? = null,
    val disc_number: Int? = null,
    val duration_ms: Int? = null,
    val episode: Boolean? = null,
    val explicit: Boolean? = null,
    val external_ids: ExternalIds? = null,
    val external_urls: ExternalUrls? = null,
    val id: String? = null,
    val is_local: Boolean? = null,
    val is_playable: Boolean? = null,
    val linked_from: LinkedFrom? = null,
    val name: String? = null,
    val popularity: Int? = null,
    val preview_url: String? = null,
    val track: Boolean? = null,
    val track_number: Int? = null,
    val type: String? = null,
    val uri: String? = null,
) : Parcelable