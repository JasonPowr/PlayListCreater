package org.wit.playlistcreater.ui.createPlaylist

import androidx.lifecycle.ViewModel
import org.wit.playlistcreater.models.AppManager
import org.wit.playlistcreater.models.playlistModel.PlaylistModel

class CreatePlaylistViewModel : ViewModel() {

    fun createPlaylist(playlist: PlaylistModel) {
        AppManager.createPlaylist(playlist)
    }

    fun updatePlaylist(playlistId: Long, updatedPlaylist: PlaylistModel){
        AppManager.updatePlaylist(playlistId, updatedPlaylist)
    }

}