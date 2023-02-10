package org.wit.playlistcreater.ui.songInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.wit.playlistcreater.models.AppManager
import org.wit.playlistcreater.models.playlistModel.PlaylistModel
import org.wit.playlistcreater.models.songModel.Songs

class SongInfoViewModel : ViewModel() {
    private val songs = MutableLiveData<Songs>()

    val observableSongs: LiveData<Songs>
        get() = songs

    fun getSong(id: String) {
        songs.value = AppManager.findSongByID(id)
    }

    fun deleteSongFromPlaylist(songId: String, playlist: PlaylistModel){
        AppManager.deleteSongFromPlaylist(songId, playlist)
    }

    fun getPlaylist(playlistId: Long): PlaylistModel? {
        return AppManager.findPlaylistById(playlistId)
    }

}