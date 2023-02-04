package org.wit.playlistcreater.models.api

import org.wit.playlistcreater.BuildConfig
import org.wit.playlistcreater.models.songModel.SongModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface ApiInterface {

    @Headers(
        "X-RapidAPI-Key: ${BuildConfig.X_RapidAPI_Key}",
        "X-RapidAPI-Host: ${BuildConfig.X_RapidAPI_Host}",
    )
    @GET("playlist_tracks/?id=37i9dQZEVXbKM896FDX8L1&offset=0&limit=100")
    fun getSongs(): Call<SongModel>
}