package org.wit.playlistcreater.ui.publicPlaylistView

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.wit.playlistcreater.models.AppManager
import org.wit.playlistcreater.models.publicPlaylistModel.PublicPlaylistModel
import org.wit.playlistcreater.models.songModel.Songs

class PublicPlaylistViewViewModel : ViewModel() {
    private val playlistSongs = MutableLiveData<List<Songs?>>()

    val observablePlaylistSongs: LiveData<List<Songs?>>
        get() = playlistSongs

    fun getSongsInPlaylist(publicId: String) {
        playlistSongs.value = AppManager.getAllSongsInPublicPlaylist(publicId)
    }

    fun getPublicPlaylist(publicId: String): PublicPlaylistModel? {
        return AppManager.getPublicPlaylist(publicId)
    }

    fun updateLikeCount(publicId: String) {
        AppManager.updateLikeCount(publicId)
    }

    fun isPlaylistLiked(publicId: String): Boolean {
        return AppManager.isPlaylistLiked(publicId)
    }

    fun unlikePlaylist(publicId: String) {
        AppManager.unlikePlaylist(publicId)
    }
}