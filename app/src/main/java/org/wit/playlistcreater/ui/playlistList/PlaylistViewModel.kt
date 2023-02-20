package org.wit.playlistcreater.ui.playlistList

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.wit.playlistcreater.models.AppManager
import org.wit.playlistcreater.models.playlistModel.PlaylistModel

class PlaylistViewModel : ViewModel() {

    private val playlistList = MutableLiveData<List<PlaylistModel>>()

    val observablePlaylistList: LiveData<List<PlaylistModel>>
        get() = playlistList

    init {
        Handler().postDelayed({
            load()
        }, 3000) //Allows time for the playlists to be read from db
    }

    fun load() {
        playlistList.value = AppManager.findAllPlaylistsInStore()
    }

    fun addSongToPlaylist(songId: String, playlist: PlaylistModel): Boolean {
        return AppManager.addSongToPlaylist(songId, playlist)
    }

    fun getIsLoaded(): Boolean {
        return AppManager.isLoaded
    }
}