package org.wit.playlistcreater.ui.songList

import android.os.Handler
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
        Handler().postDelayed({
            songs.value =
                AppManager.findAllSongsInStore()  //delay a bit so that the getsongs function has time to retrieve data from the api and load it into mem
        }, 2000)
        //https://stackoverflow.com/questions/43348623/how-to-call-a-function-after-delay-in-kotlin
    }
}