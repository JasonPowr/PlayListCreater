package org.wit.playlistcreater.ui.publicPlaylists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.wit.playlistcreater.models.AppManager
import org.wit.playlistcreater.models.publicPlaylistModel.PublicPlaylistModel

class PublicPlaylistsViewModel : ViewModel() {


    private val publicPlaylistsList = MutableLiveData<List<PublicPlaylistModel>>()

    val observablePlaylistList: LiveData<List<PublicPlaylistModel>>
        get() = publicPlaylistsList

    fun load() {
        publicPlaylistsList.value = AppManager.getAllPublicPlaylists()
    }

    fun getPublicPlaylistsFromDB() {
        AppManager.getAllPublicPlaylistsFromDb()
        android.os.Handler().postDelayed({
            load()
        }, 2000)
    }

}