package org.wit.playlistcreater.models

import org.wit.playlistcreater.models.playlistModel.PlaylistModel
import org.wit.playlistcreater.models.songModel.Song
import org.wit.playlistcreater.models.songModel.SongModel


interface AppStore {
    fun createPlaylist(newPlaylist: PlaylistModel)
    fun updatePlaylist(playlistId: Long, updatedPlaylist: PlaylistModel)
    fun findAllPlaylistsInStore() : List<PlaylistModel>
    fun findAllSongsInStore() : List<Song?>
    fun addAllSongsToStore(songItemList: List<SongModel?>)
    fun findPlaylistById(playlistId: Long): PlaylistModel?
    fun findSongByID(id: String): Song?
    fun addSongToPlaylist(songId: String, playlist: PlaylistModel) : Boolean
    fun findAllSongsInPlaylist(playlistId: Long) : MutableList<Song>
}
