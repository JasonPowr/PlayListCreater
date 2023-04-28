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

    fun deletePlaylist(playlist: PlaylistModel) {
        AppManager.deletePlaylist(playlist)
    }

    fun sharePlaylist(playlist: PlaylistModel) {
        AppManager.sharePlaylist(playlist)
    }

    fun stopShare(playlist: PlaylistModel) {
        AppManager.stopSharePlaylist(playlist)
    }

    fun swipeDelete(songID: String, playlist: PlaylistModel) {
        AppManager.deleteSongFromPlaylist(songID, playlist)
    }

}