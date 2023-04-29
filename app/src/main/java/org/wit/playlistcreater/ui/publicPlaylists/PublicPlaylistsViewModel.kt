package org.wit.playlistcreater.ui.publicPlaylists

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.wit.playlistcreater.models.AppManager
import org.wit.playlistcreater.models.publicPlaylistModel.PublicPlaylistModel

class PublicPlaylistsViewModel : ViewModel() {


    private val publicPlaylistsList = MutableLiveData<List<PublicPlaylistModel>>()
    val observablePlaylistList: LiveData<List<PublicPlaylistModel>>
        get() = publicPlaylistsList
    var isLoading: Boolean = false


    init {
        Handler().postDelayed({
            load()
        }, 2000)
    }

    fun load() {
        publicPlaylistsList.value = AppManager.findAllPublicPlaylistsInStore()
    }

    fun getPlaylistsFromDB() {
        isLoading = true
        AppManager.getAllPublicPlaylistsFromDb()
        Handler().postDelayed({
            load()
            isLoading = false
        }, 2000)
    }

    fun getIsLoading(): Boolean {
        return isLoading
    }

}