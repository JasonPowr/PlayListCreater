package org.wit.playlistcreater.ui.songList

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.wit.playlistcreater.models.AppManager
import org.wit.playlistcreater.models.songModel.Songs

class SongViewModel : ViewModel() {

    private val songsOption1 = MutableLiveData<List<Songs?>>()
    val observableSongsOption1: LiveData<List<Songs?>>
        get() = songsOption1

    private val songsOption2 = MutableLiveData<List<Songs?>>()
    val observableSongsOption2: LiveData<List<Songs?>>
        get() = songsOption2

    private val songsOption3 = MutableLiveData<List<Songs?>>()
    val observableSongsOption3: LiveData<List<Songs?>>
        get() = songsOption3

    init {
        load()
    }

    fun load() {
        Handler().postDelayed({
            songsOption1.value =
                AppManager.getIrelandsTop50FromMem()  //delay a bit so that the get songs function has time to retrieve data from the api and load it into mem
            songsOption2.value = AppManager.getSpotifyTop50FromMem()
            songsOption3.value = AppManager.getNewReleasesFromMem()
        }, 2000)
        //https://stackoverflow.com/questions/43348623/how-to-call-a-function-after-delay-in-kotlin
    }


}