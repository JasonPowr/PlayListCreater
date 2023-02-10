package org.wit.playlistcreater.models.playlistModel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.wit.playlistcreater.models.songModel.Songs

@Parcelize
data class PlaylistModel(
    var id: Long,
    var title: String,
    val songs: MutableList<Songs>
) : Parcelable
