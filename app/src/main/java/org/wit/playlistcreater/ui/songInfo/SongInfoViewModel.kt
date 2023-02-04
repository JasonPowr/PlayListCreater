package org.wit.playlistcreater.ui.songInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.wit.playlistcreater.models.AppManager
import org.wit.playlistcreater.models.songModel.Song

class SongInfoViewModel : ViewModel() {
    private val song = MutableLiveData<Song>()

    val observableSong: LiveData<Song>
        get() = song

    fun getSong(id: String) {
        song.value = AppManager.findSongByID(id)
    }
}