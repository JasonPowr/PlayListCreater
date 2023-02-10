package org.wit.playlistcreater.models.playlistModel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.wit.playlistcreater.models.songModel.Song

@Parcelize
data class PlaylistModel(
    var id: Long,
    val title: String,
    val songs: MutableList<Song>
) : Parcelable
