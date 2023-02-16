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
        }, 3000)
    }

    fun load() {
        playlistList.value = AppManager.findAllPlaylistsInStore()
    }


    fun addSongToPlaylist(songId: String, playlist: PlaylistModel): Boolean {
        return AppManager.addSongToPlaylist(songId, playlist)
    }
}