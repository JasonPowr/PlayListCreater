package org.wit.playlistcreater.models

import org.wit.playlistcreater.models.songModel.Song
import org.wit.playlistcreater.models.songModel.SongModel


interface SongStore {
    fun findAll() : List<Song?>
    fun addAll(songItemList: List<SongModel?>)
}
