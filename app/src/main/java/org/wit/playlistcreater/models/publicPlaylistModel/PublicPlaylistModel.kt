package org.wit.playlistcreater.models.publicPlaylistModel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.wit.playlistcreater.models.playlistModel.PlaylistModel

@Parcelize
data class PublicPlaylistModel(
    val uid: String,
    val profilePic: String,
    var playlist: PlaylistModel,
): Parcelable

