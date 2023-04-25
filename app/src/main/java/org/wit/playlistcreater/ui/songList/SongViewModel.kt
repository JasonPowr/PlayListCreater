package org.wit.playlistcreater.ui.songList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.wit.playlistcreater.models.AppManager
import org.wit.playlistcreater.models.songModel.Songs

class SongViewModel : ViewModel() {

    private val songs = MutableLiveData<List<Songs?>>()

    val observableSongs: LiveData<List<Songs?>>
        get() = songs

    init {
        load()
    }

    fun load() {
        songs.value = AppManager.findAllSongsInStore()
    }
}