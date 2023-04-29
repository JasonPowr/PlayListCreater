package org.wit.playlistcreater.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.wit.playlistcreater.models.AppManager
import org.wit.playlistcreater.models.publicPlaylistModel.PublicPlaylistModel

class ProfileViewModel : ViewModel() {
    val currentUser = AppManager.auth.currentUser

    private val likedPlaylistsList = MutableLiveData<List<PublicPlaylistModel>>()
    val observableLikedPlaylistList: LiveData<List<PublicPlaylistModel>>
        get() = likedPlaylistsList
    
    fun load() {
        likedPlaylistsList.value = AppManager.getAllLikedPlaylists()
    }


}