package org.wit.playlistcreater.api

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
    fun getIrelandsTop50(): Call<SongModel>

    @Headers(
        "X-RapidAPI-Key: ${BuildConfig.X_RapidAPI_Key}",
        "X-RapidAPI-Host: ${BuildConfig.X_RapidAPI_Host}",
    )
    @GET("playlist_tracks/?id=4OIVU71yO7SzyGrh0ils2i&offset=0&limit=100")
    fun getSpotifyTop50(): Call<SongModel>

    @Headers(
        "X-RapidAPI-Key: ${BuildConfig.X_RapidAPI_Key}",
        "X-RapidAPI-Host: ${BuildConfig.X_RapidAPI_Host}",
    )
    @GET("playlist_tracks/?id=593HKP3qHQXS0RLZmeeHly&offset=0&limit=100")
    fun getNewReleases(): Call<SongModel>
}