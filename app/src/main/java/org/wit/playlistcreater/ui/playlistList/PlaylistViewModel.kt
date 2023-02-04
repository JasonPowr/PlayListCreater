package org.wit.playlistcreater.ui.playlistList

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
        load()
    }

    fun load() {
        playlistList.value = AppManager.findAllPlaylistsInStore()
    }
}