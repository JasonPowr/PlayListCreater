package org.wit.playlistcreater.models

import org.wit.playlistcreater.models.playlistModel.PlaylistModel
import org.wit.playlistcreater.models.songModel.Song
import org.wit.playlistcreater.models.songModel.SongModel


var lastId = 0L
internal fun getId(): Long {
    return lastId++
}

object AppManager : AppStore {

    val playlists = ArrayList<PlaylistModel>()
    val songs = ArrayList<SongModel?>()

    override fun createPlaylist(newPlaylist: PlaylistModel) {
        playlists.add(newPlaylist)
    }

    override fun findAllPlaylistsInStore(): List<PlaylistModel> {
        return playlists
    }

    override fun findAllSongsInStore(): List<Song?> {
        return songs[0]!!.items
    }

    override fun addAllSongsToStore(songItemList: List<SongModel?>) {
        songs.addAll(songItemList)
    }

    override fun findSongByID(id: String): Song? {
        return songs[0]!!.items.find { s -> s.track.id == id  }
    }

}