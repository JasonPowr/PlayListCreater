package org.wit.playlistcreater.ui.playlistSongList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.wit.playlistcreater.models.AppManager
import org.wit.playlistcreater.models.playlistModel.PlaylistModel
import org.wit.playlistcreater.models.songModel.Song

class PlaylistSongViewViewModel : ViewModel() {
    private val playlistSongList = MutableLiveData<List<Song?>>()

    val observablePlaylistSongList: LiveData<List<Song?>>
        get() = playlistSongList

    fun getSongsInPlaylist(playlistId: Long) {
        playlistSongList.value = AppManager.findAllSongsInPlaylist(playlistId)
    }

}