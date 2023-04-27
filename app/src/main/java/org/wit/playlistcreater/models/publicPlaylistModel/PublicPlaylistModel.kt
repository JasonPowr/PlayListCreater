package org.wit.playlistcreater.models.publicPlaylistModel

import org.wit.playlistcreater.models.playlistModel.PlaylistModel

data class PublicPlaylistModel(
    val uid: String,
    val profilePic: String,
    var playlist: PlaylistModel,
)

