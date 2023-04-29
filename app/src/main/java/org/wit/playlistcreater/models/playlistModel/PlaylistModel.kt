package org.wit.playlistcreater.models.playlistModel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.wit.playlistcreater.models.songModel.Songs

@Parcelize
data class PlaylistModel(
    var id: Long? = null,
    var playListGenre: String? = null,
    var title: String? = null,
    var songs: MutableList<Songs>? = null,
    var isShared: Boolean? = null,
    var publicID: String? = null,
) : Parcelable
