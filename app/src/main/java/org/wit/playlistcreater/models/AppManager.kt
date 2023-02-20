package org.wit.playlistcreater.models

import android.annotation.SuppressLint
import android.os.Handler
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.wit.playlistcreater.models.api.ApiInterface
import org.wit.playlistcreater.models.api.Retro
import org.wit.playlistcreater.models.playlistModel.PlaylistModel
import org.wit.playlistcreater.models.songModel.SongModel
import org.wit.playlistcreater.models.songModel.Songs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


var lastId = 0L
internal fun getId(): Long {
    return lastId++
}

object AppManager : AppStore {

    @SuppressLint("StaticFieldLeak")
    private var db = Firebase.firestore
    private var auth: FirebaseAuth = Firebase.auth
    var isLoaded: Boolean = false

    private val playlists = ArrayList<PlaylistModel>()
    val songs = ArrayList<SongModel?>()

    init {
        getSongs()
        Handler().postDelayed({
            getAllPlaylistsFromDb() //allows for enough time to load all songs and playlists
        }, 2000)
    }

    private fun getSongs() {
        if (songs.isEmpty()) {
            val listOfSongs = mutableListOf<SongModel?>()
            val retro = Retro().getRetroClient().create(ApiInterface::class.java)
            retro.getSongs().enqueue(object : Callback<SongModel> {
                override fun onResponse(
                    call: Call<SongModel>,
                    response: Response<SongModel>
                ) {
                    listOfSongs.addAll(listOf(response.body()))
                    addAllSongsToStore(listOfSongs)
                }

                override fun onFailure(call: Call<SongModel>, t: Throwable) {
                    Log.e("API", t.message.toString())
                }
            })
        }
    }

    override fun createUser(uid: String, email: String) {
        val user = hashMapOf(
            "uid" to uid,
            "email" to email
        )
        db.collection("users").document(user["uid"].toString()).set(user)
    }

    override fun createPlaylist(newPlaylist: PlaylistModel) {
        newPlaylist.id = playlists.size.toLong()
        playlists.add(newPlaylist)

        db.collection("users").document(auth.currentUser!!.uid).collection("playlists")
            .document(newPlaylist.id.toString()).set(newPlaylist)
    }

    override fun getAllPlaylistsFromDb(): List<PlaylistModel> {
        if (playlists.isEmpty()) {
            db.collection("users").document(auth.currentUser!!.uid).collection("playlists")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val songData = document.data["songs"] as List<*>
                        playlists.add(
                            PlaylistModel(
                                document.id.toLong(),
                                document.data["playListGenre"].toString(),
                                document.data["title"].toString(),
                                mutableListOf()
                            )
                        )
                        addBackIntoPlaylist(document.id.toLong(), songData)
                    }
                    isLoaded = true
                }
                .addOnFailureListener { exception ->
                    Log.e("data", "Error getting playlists: ", exception)
                }
        }
        return playlists
    }

    private fun addBackIntoPlaylist(playlistId: Long, songIdList: List<*>) {
        val foundPlaylist = findPlaylistById(playlistId)

        for (id in songIdList) {
            if (songs.isNotEmpty())
                foundPlaylist!!.songs.add(findSongByID(id.toString())!!)
        }

    }

    override fun findAllSongsInPlaylist(playlistId: Long): MutableList<Songs> {
        val foundPlaylist = findPlaylistById(playlistId)
        return foundPlaylist!!.songs
    }

    override fun findAllPlaylistsInStore(): List<PlaylistModel> {
        return playlists
    }

    override fun updatePlaylist(playlistId: Long, updatedPlaylist: PlaylistModel) {
        val foundPlaylist = findPlaylistById(playlistId)
        if (foundPlaylist != null) {
            foundPlaylist.title = updatedPlaylist.title
            foundPlaylist.playListGenre = updatedPlaylist.playListGenre
            db.collection("users").document(auth.currentUser!!.uid).collection("playlists")
                .document(playlistId.toString()).update("title", updatedPlaylist.title)
        }
    }

    override fun deletePlaylist(playlistId: Long) {
        val foundPlaylist = findPlaylistById(playlistId)
        if (foundPlaylist != null) {
            playlists.remove(foundPlaylist)
            db.collection("users").document(auth.currentUser!!.uid).collection("playlists")
                .document(playlistId.toString()).delete()
        }
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
        return songs[0]!!.items.find { s -> s.track.id == id }
    }

    override fun addSongToPlaylist(songId: String, playlist: PlaylistModel): Boolean {
        val foundSong = findSongByID(songId)
        if (foundSong != null) {

            foundSong.isInPlaylist = true
            playlist.songs.add(foundSong)

            db.collection("users").document(auth.currentUser!!.uid).collection("playlists")
                .document(playlist.id.toString())
                .update("songs", FieldValue.arrayUnion(foundSong.track.id))
            return true

        }
        return false
    }


    override fun deleteSongFromPlaylist(songId: String, playlist: PlaylistModel) {
        val foundSong = findSongByID(songId)
        foundSong!!.isInPlaylist = false
        playlist.songs.remove(foundSong)

        db.collection("users").document(auth.currentUser!!.uid).collection("playlists")
            .document(playlist.id.toString())
            .update("songs", FieldValue.arrayRemove(foundSong.track.id))
    }

    fun deleteAll() {
        playlists.removeAll(findAllPlaylistsInStore())
    }


}