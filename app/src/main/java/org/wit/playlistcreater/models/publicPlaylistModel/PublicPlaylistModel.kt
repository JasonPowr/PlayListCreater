package org.wit.playlistcreater.models.publicPlaylistModel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.wit.playlistcreater.models.playlistModel.PlaylistModel

@Parcelize
data class PublicPlaylistModel(
    var uid: String? = null,
    val profilePic: String? = null,
    var playlist: PlaylistModel? = null,
    var displayName: String? = null,
    var likes: Int? = null,
) : Parcelable

