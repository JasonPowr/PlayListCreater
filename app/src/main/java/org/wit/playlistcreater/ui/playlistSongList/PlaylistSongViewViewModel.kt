package org.wit.playlistcreater.ui.playlistSongList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.wit.playlistcreater.models.AppManager
import org.wit.playlistcreater.models.playlistModel.PlaylistModel
import org.wit.playlistcreater.models.songModel.Songs

class PlaylistSongViewViewModel : ViewModel() {
    private val playlistSongs = MutableLiveData<List<Songs?>>()

    val observablePlaylistSongs: LiveData<List<Songs?>>
        get() = playlistSongs

    fun getSongsInPlaylist(playlistId: Long) {
        playlistSongs.value = AppManager.findAllSongsInPlaylist(playlistId)
    }

    fun getPlaylist(playlistId: Long): PlaylistModel? {
        return AppManager.findPlaylistById(playlistId)
    }

    fun deletePlaylist(playlistId: Long) {
        AppManager.deletePlaylist(playlistId)
    }

}