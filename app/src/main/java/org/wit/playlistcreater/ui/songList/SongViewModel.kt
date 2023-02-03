package org.wit.playlistcreater.ui.songList

import android.os.Handler
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.wit.playlistcreater.models.ApiInterface
import org.wit.playlistcreater.models.Retro
import org.wit.playlistcreater.models.SongManager
import org.wit.playlistcreater.models.songModel.Song
import org.wit.playlistcreater.models.songModel.SongModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SongViewModel : ViewModel() {

    private val songList = MutableLiveData<List<Song?>>()

    val observableSongsList: LiveData<List<Song?>>
        get() = songList

    init {
        load()
    }

    fun load() {
        getSongs()
        Handler().postDelayed({
            songList.value = SongManager.findAll()  //delay a bit so that the get songs function has time to retrieve data from the api and load it into mem
        }, 3000)
        //https://stackoverflow.com/questions/43348623/how-to-call-a-function-after-delay-in-kotlin
    }

    private fun getSongs() {
        if (SongManager.songs.isEmpty()) {
            val listOfSongs = mutableListOf<SongModel?>()
            val retro = Retro().getRetroClient().create(ApiInterface::class.java)
            retro.getSongs().enqueue(object : Callback<SongModel> {
                override fun onResponse(
                    call: Call<SongModel>,
                    response: Response<SongModel>
                ) {
                        listOfSongs.addAll(listOf(response.body()))
                        SongManager.addAll(listOfSongs)
                }

                override fun onFailure(call: Call<SongModel>, t: Throwable) {
                    Log.e("API", t.message.toString())
                }

            })
        }
    }

}