package org.wit.playlistcreater.models

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.wit.playlistcreater.api.ApiInterface
import org.wit.playlistcreater.api.Retro
import org.wit.playlistcreater.firebase.FirebaseImageManager
import org.wit.playlistcreater.models.playlistModel.PlaylistModel
import org.wit.playlistcreater.models.publicPlaylistModel.PublicPlaylistModel
import org.wit.playlistcreater.models.songModel.SongModel
import org.wit.playlistcreater.models.songModel.Songs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


var lastId = 0L
internal fun getId(): Long {
    return lastId++
}

object AppManager : AppStore {

    @SuppressLint("StaticFieldLeak")
    private var db = Firebase.firestore
    var auth: FirebaseAuth = Firebase.auth

    private val playlists = ArrayList<PlaylistModel>()
    private val publicPlaylists = ArrayList<PublicPlaylistModel>()
    val songs = ArrayList<SongModel?>()

    fun getSongs() {
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
        db.collection("users").document(auth.currentUser!!.uid).collection("playlists")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val songData = document.data["songs"] as List<*>
                    val playlist = PlaylistModel(
                        document.id.toLong(),
                        document.data["playListGenre"].toString(),
                        document.data["title"].toString(),
                        mutableListOf(),
                        document.data["isShared"].toString().toBoolean(),
                        document.data["publicID"].toString(),
                    )
                    playlists.add(playlist)
                    addBackIntoPlaylist(playlist, songData)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("data", "Error getting playlists: ", exception)
            }
        return playlists
    }

    private fun addBackIntoPlaylist(playlist: PlaylistModel, songIdList: List<*>) {
        for (id in songIdList) {
            playlist.songs.add(findSongByID(id.toString())!!)
        }
    }

    override fun getAllPublicPlaylistsFromDb(): List<PublicPlaylistModel> {
        publicPlaylists.clear()
        db.collection("publicPlaylists").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val dbPlaylist = document.data.getValue("playlist") as Map<*, *>
                    val uid = document.data.getValue("uid").toString()
                    val profilePic = document.data.getValue("profilePic").toString()
                    val songData = dbPlaylist["songs"] as List<*>

                    val playlist = PlaylistModel(
                        dbPlaylist["id"].toString().toLong(),
                        dbPlaylist["playListGenre"].toString(),
                        dbPlaylist["title"].toString(),
                        mutableListOf(),
                        dbPlaylist["isShared"].toString().toBoolean(),
                        dbPlaylist["publicID"].toString(),
                    )
                    addBackIntoPublicPlaylist(playlist, songData)

                    val publicPlaylist = PublicPlaylistModel(uid, profilePic, playlist)
                    publicPlaylists.add(publicPlaylist)
                }
            }
        return publicPlaylists
    }

    private fun addBackIntoPublicPlaylist(playlist: PlaylistModel, songIdList: List<*>) {
        for (id in songIdList) {
            if (songs.isNotEmpty())
                playlist.songs.add(findSongByID(id.toString())!!)
        }
    }

    override fun sharePlaylist(playlist: PlaylistModel) {
        val publicID = UUID.randomUUID().toString()
        playlist.publicID = publicID
        playlist.isShared = true

        db.collection("users").document(auth.currentUser!!.uid).collection("playlists")
            .document(playlist.id.toString()).update(
                "publicID", publicID,
                "isShared", true
            )

        val publicPlaylist = PublicPlaylistModel(
            auth.currentUser!!.uid,
            FirebaseImageManager.imageUri.value.toString(), playlist
        )
        db.collection("publicPlaylists").document(publicID).set(publicPlaylist)

        for (song in playlist.songs) {
            db.collection("publicPlaylists").document(publicID)
                .update("playlist.songs", FieldValue.arrayRemove(song))

            db.collection("publicPlaylists").document(publicID)
                .update("playlist.songs", FieldValue.arrayUnion(song.track.id))
        }

    }

    override fun stopSharePlaylist(playlist: PlaylistModel) {
        db.collection("users").document(auth.currentUser!!.uid).collection("playlists")
            .document(playlist.id.toString()).update(
                "publicID", "",
                "isShared", false
            )

        db.collection("publicPlaylists").document(playlist.publicID).delete()

        playlist.publicID = ""
        playlist.isShared = false
    }

    override fun getAllPublicPlaylists(): List<PublicPlaylistModel> {
        return publicPlaylists
    }

    override fun removeAllPublicPlaylistsFromMem() {
        publicPlaylists.removeAll(getAllPublicPlaylists().toSet())
    }

    //https://developer.android.com/training/articles/user-data-ids

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
                .document(playlistId.toString()).update(
                    "title", updatedPlaylist.title,
                    "playListGenre", updatedPlaylist.playListGenre,
                )
        }
    }


    override fun deletePlaylist(playlist: PlaylistModel) {
        db.collection("users").document(auth.currentUser!!.uid).collection("playlists")
            .document(playlist.id.toString()).delete()

        if (playlist.isShared) {
            db.collection("publicPlaylists").document(playlist.publicID).delete()
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


            if (playlist.isShared) {
                db.collection("publicPlaylists").document(playlist.publicID)
                    .update("playlist.songs", FieldValue.arrayUnion(foundSong.track.id))
            }

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


        if (playlist.isShared) {
            db.collection("publicPlaylists").document(playlist.publicID)
                .update("playlist.songs", FieldValue.arrayRemove(foundSong.track.id))
        }

    }

    fun deleteAll() {
        playlists.clear()
    }

    override fun updateImageRef(userid: String, imageUri: String) {
        for (playlist in playlists) {
            if (playlist.isShared) {
                db.collection("publicPlaylists").document(playlist.publicID)
                    .update("profilePic", imageUri)
            }

        }
    }

}

//https://firebase.google.com/docs/firestore/manage-data/add-data