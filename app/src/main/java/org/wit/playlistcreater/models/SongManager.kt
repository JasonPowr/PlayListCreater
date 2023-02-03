package org.wit.playlistcreater.models

import org.wit.playlistcreater.models.songModel.Song
import org.wit.playlistcreater.models.songModel.SongModel


var lastId = 0L
internal fun getId(): Long {
    return lastId++
}

object SongManager : SongStore {

    val songs = ArrayList<SongModel?>()

    override fun findAll(): List<Song?> {
        return songs[0]!!.items
    }

    override fun addAll(songItemList: List<SongModel?>) {
        songs.addAll(songItemList)
    }

}