package org.wit.playlistcreater.models

import org.wit.playlistcreater.models.playlistModel.PlaylistModel
import org.wit.playlistcreater.models.songModel.Songs
import org.wit.playlistcreater.models.songModel.SongModel


var lastId = 0L
internal fun getId(): Long {
    return lastId++
}

object AppManager : AppStore {

    val playlists = ArrayList<PlaylistModel>()
    val songs = ArrayList<SongModel?>()

    override fun createPlaylist(newPlaylist: PlaylistModel) {
        newPlaylist.id = getId()
        playlists.add(newPlaylist)
    }

    override fun updatePlaylist(playlistId: Long, updatedPlaylist: PlaylistModel) {
       val foundPlaylist = findPlaylistById(playlistId)
        if (foundPlaylist != null) {
            foundPlaylist.title = updatedPlaylist.title
        }
    }

    override fun deletePlaylist(playlistId: Long) {
        val foundPlaylist = findPlaylistById(playlistId)
        if (foundPlaylist != null) {
            playlists.remove(foundPlaylist)
        }
    }

    override fun findAllPlaylistsInStore(): List<PlaylistModel> {
        return playlists
    }

    override fun findAllSongsInStore(): List<Songs?> {
        return songs[0]!!.items
    }

    override fun addAllSongsToStore(songItemList: List<SongModel?>) {
        songs.addAll(songItemList)
    }

    override fun findPlaylistById(playlistId: Long): PlaylistModel? {
        return playlists.find { p -> p.id == playlistId }
    }

    override fun findSongByID(id: String): Songs? {
        return songs[0]!!.items.find { s -> s.track.id == id  }
    }

    override fun addSongToPlaylist(songId: String, playlist: PlaylistModel) : Boolean {
        val foundSong = findSongByID(songId)
        if (foundSong != null) {
            foundSong.isInPlaylist = true
            playlist.songs.add(foundSong)
            return true
        }
        return false
    }

    override fun findAllSongsInPlaylist(playlistId: Long): MutableList<Songs> {
       val foundPlaylist = findPlaylistById(playlistId)
        return foundPlaylist!!.songs
    }

    override fun deleteSongFromPlaylist(songId: String, playlist: PlaylistModel) {
        val foundSong = findSongByID(songId)
        foundSong!!.isInPlaylist = false
        playlist.songs.remove(foundSong)
    }

}