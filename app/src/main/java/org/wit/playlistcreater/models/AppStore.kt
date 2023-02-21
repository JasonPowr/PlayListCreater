package org.wit.playlistcreater.models

import org.wit.playlistcreater.models.playlistModel.PlaylistModel
import org.wit.playlistcreater.models.songModel.SongModel
import org.wit.playlistcreater.models.songModel.Songs


interface AppStore {
    fun createPlaylist(newPlaylist: PlaylistModel)
    fun updatePlaylist(playlistId: Long, updatedPlaylist: PlaylistModel)
    fun deletePlaylist(playlistId: Long)
    fun findAllPlaylistsInStore(): List<PlaylistModel>
    fun findAllSongsInStore(): List<Songs?>
    fun addAllSongsToStore(songItemList: List<SongModel?>)
    fun findPlaylistById(playlistId: Long): PlaylistModel?
    fun findSongByID(id: String): Songs?
    fun addSongToPlaylist(songId: String, playlist: PlaylistModel): Boolean
    fun findAllSongsInPlaylist(playlistId: Long): MutableList<Songs>
    fun deleteSongFromPlaylist(songId: String, playlist: PlaylistModel)
    fun createUser(uid: String, email: String)
    fun getAllPlaylistsFromDb(): List<PlaylistModel>
}
