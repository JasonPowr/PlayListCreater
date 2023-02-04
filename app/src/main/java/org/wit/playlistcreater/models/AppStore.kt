package org.wit.playlistcreater.models

import org.wit.playlistcreater.models.playlistModel.PlaylistModel
import org.wit.playlistcreater.models.songModel.Song
import org.wit.playlistcreater.models.songModel.SongModel


interface AppStore {
    fun createPlaylist(newPlaylist: PlaylistModel)
    fun findAllPlaylistsInStore() : List<PlaylistModel>
    fun findAllSongsInStore() : List<Song?>
    fun addAllSongsToStore(songItemList: List<SongModel?>)
}
