package org.wit.playlistcreater.models

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
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
    val irelandsTop50 = ArrayList<SongModel?>()
    val spotifyTop50 = ArrayList<SongModel?>()
    val newReleases = ArrayList<SongModel?>()
    val likedPlaylists = ArrayList<PublicPlaylistModel>()

    fun getIrelandsTop50() {
        if (songs.isEmpty()) {
            val listOfSongs = mutableListOf<SongModel?>()
            val retro = Retro().getRetroClient().create(ApiInterface::class.java)
            retro.getIrelandsTop50().enqueue(object : Callback<SongModel> {
                override fun onResponse(
                    call: Call<SongModel>,
                    response: Response<SongModel>
                ) {
                    listOfSongs.addAll(listOf(response.body()))
                    addAllSongsToStore(listOfSongs)
                    irelandsTop50.addAll(listOfSongs)
                }

                override fun onFailure(call: Call<SongModel>, t: Throwable) {
                    Log.e("API", t.message.toString())
                }
            })
        }
    }

    fun getIrelandsTop50FromMem(): List<Songs> {
        return irelandsTop50[0]!!.items!!
    }

    fun getSpotifyTop50() {
        val listOfSongs = mutableListOf<SongModel?>()
        val retro = Retro().getRetroClient().create(ApiInterface::class.java)
        retro.getSpotifyTop50().enqueue(object : Callback<SongModel> {
            override fun onResponse(
                call: Call<SongModel>,
                response: Response<SongModel>
            ) {
                listOfSongs.addAll(listOf(response.body()))
                songs.addAll(listOfSongs)
                spotifyTop50.addAll(listOfSongs)
            }

            override fun onFailure(call: Call<SongModel>, t: Throwable) {
                Log.e("API", t.message.toString())
            }
        })
    }

    fun getSpotifyTop50FromMem(): List<Songs> {
        return spotifyTop50[0]!!.items!!
    }

    fun getNewReleases() {
        val listOfSongs = mutableListOf<SongModel?>()
        val retro = Retro().getRetroClient().create(ApiInterface::class.java)
        retro.getNewReleases().enqueue(object : Callback<SongModel> {
            override fun onResponse(
                call: Call<SongModel>,
                response: Response<SongModel>
            ) {
                listOfSongs.addAll(listOf(response.body()))
                songs.addAll(listOfSongs)
                newReleases.addAll(listOfSongs)
            }

            override fun onFailure(call: Call<SongModel>, t: Throwable) {
                Log.e("API", t.message.toString())
            }
        })
    }


    fun getNewReleasesFromMem(): List<Songs> {
        return newReleases[0]!!.items!!
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
        playlists.clear()
        db.collection("users").document(auth.currentUser!!.uid).collection("playlists")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val playlist = document.toObject<PlaylistModel>()
                    playlists.add(playlist)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("data", "Error getting playlists: ", exception)
            }



        db.collection("users").document(auth.currentUser!!.uid).collection("likedPlaylists")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val likedPlaylist = document.toObject<PublicPlaylistModel>()
                    likedPlaylists.add(likedPlaylist)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("data", "Error getting playlists: ", exception)
            }

        return playlists
    }


    override fun getAllPublicPlaylistsFromDb(): List<PublicPlaylistModel> {
        publicPlaylists.clear()
        db.collection("publicPlaylists").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val publicPlaylist = document.toObject<PublicPlaylistModel>()
                    publicPlaylists.add(publicPlaylist)
                }
            }
        return publicPlaylists
    }

    fun findAllPublicPlaylistsInStore(): List<PublicPlaylistModel>? {
        return publicPlaylists
    }

    fun getPublicPlaylist(publicId: String): PublicPlaylistModel? {
        return publicPlaylists.find { p -> p.playlist!!.publicID == publicId }
    }

    fun isPlaylistLiked(publicId: String): Boolean {
        for (publicPlaylist in publicPlaylists) {
            for (likedPlaylist in likedPlaylists) {
                if (publicPlaylist.playlist?.publicID.toString()
                        .equals(likedPlaylist.playlist?.publicID.toString())
                ) {
                    return true
                }
            }
        }
        return false
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
            FirebaseImageManager.imageUri.value.toString(), playlist,
            auth.currentUser!!.displayName!!,
            0
        )
        db.collection("publicPlaylists").document(publicID).set(publicPlaylist)

    }

    override fun stopSharePlaylist(playlist: PlaylistModel) {
        db.collection("users").document(auth.currentUser!!.uid).collection("playlists")
            .document(playlist.id.toString()).update(
                "publicID", "",
                "isShared", false
            )

        db.collection("publicPlaylists").document(playlist.publicID!!).delete()

        playlist.publicID = ""
        playlist.isShared = false
    }

    fun getAllSongsInPublicPlaylist(publicId: String): List<Songs?> {
        val songs = mutableListOf<Songs?>()
        for (playlists in publicPlaylists) {
            if (playlists.playlist!!.publicID == publicId) {
                songs.addAll(playlists.playlist!!.songs!!)
            }
        }
        return songs
    }

    override fun getAllPublicPlaylists(): List<PublicPlaylistModel> {
        return publicPlaylists
    }

    override fun removeAllPublicPlaylistsFromMem() {
        publicPlaylists.removeAll(getAllPublicPlaylists().toSet())
    }


    override fun findAllSongsInPlaylist(playlistId: Long): MutableList<Songs> {
        val foundPlaylist = findPlaylistById(playlistId)
        return foundPlaylist!!.songs!!
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

        if (playlist.publicID != "") {
            db.collection("publicPlaylists").document(playlist.publicID!!).delete()
        }
    }

    override fun findAllSongsInStore(): List<Songs?> {
        return songs[0]!!.items!!
    }

    override fun addAllSongsToStore(songItemList: List<SongModel?>) {
        songs.addAll(songItemList)
    }

    override fun findPlaylistById(playlistId: Long): PlaylistModel? {
        return playlists.find { p -> p.id == playlistId }
    }

    override fun findSongByID(id: String): Songs? {
        return songs[0]!!.items!!.find { s -> s.track!!.id == id }
    }

    override fun addSongToPlaylist(songId: String, playlist: PlaylistModel): Boolean {
        val foundSong = findSongByID(songId)
        if (foundSong != null) {

            foundSong.isInPlaylist = true
            playlist.songs!!.add(foundSong)

            db.collection("users").document(auth.currentUser!!.uid).collection("playlists")
                .document(playlist.id.toString())
                .update("songs", FieldValue.arrayUnion(foundSong))


            if (playlist.publicID != "") {
                db.collection("publicPlaylists").document(playlist.publicID!!)
                    .update("playlist.songs", FieldValue.arrayUnion(foundSong))
            }

            return true

        }
        return false
    }


    override fun deleteSongFromPlaylist(songId: String, playlist: PlaylistModel) {
        val foundSong = findSongByID(songId)
        foundSong!!.isInPlaylist = false
        playlist.songs!!.remove(foundSong)
        playlist.isShared = playlist.isShared


        db.collection("users").document(auth.currentUser!!.uid).collection("playlists")
            .document(playlist.id.toString()).set(playlist)


        if (playlist.publicID != "") {
            val publicPlaylist = getPublicPlaylist(playlist.publicID.toString())
            publicPlaylist!!.playlist = playlist
            db.collection("publicPlaylists").document(playlist.publicID!!).set(publicPlaylist)
        }

    }

    fun deleteAll() {
        playlists.clear()
    }

    override fun updateImageRef(userid: String, imageUri: String) {
        for (playlist in playlists) {
            if (playlist.publicID != "") {
                db.collection("publicPlaylists").document(playlist.publicID!!)
                    .update("profilePic", imageUri)
            }

        }
    }

    override fun updateLikeCount(publicId: String) {
        for (playlist in publicPlaylists) {
            if (playlist.playlist!!.publicID == publicId) {


                db.collection("users").document(auth.currentUser!!.uid).collection("likedPlaylists")
                    .document(
                        playlist.playlist!!.publicID.toString()
                    ).set(playlist)

                db.collection("publicPlaylists").document(publicId)
                    .update("likes", playlist.likes!! + 1)

                playlist.likes = playlist.likes!! + 1

                likedPlaylists.add(playlist)

            }
        }
    }

    fun unlikePlaylist(publicId: String) {
        for (playlist in publicPlaylists) {
            if (playlist.playlist!!.publicID == publicId) {
                db.collection("users").document(auth.currentUser!!.uid).collection("likedPlaylists")
                    .document(playlist.playlist!!.publicID.toString()).delete()
                db.collection("publicPlaylists").document(publicId)
                    .update("likes", playlist.likes!! - 1)
                playlist.likes = playlist.likes!! - 1
                likedPlaylists.remove(playlist)
            }
        }
    }


}

//https://firebase.google.com/docs/firestore/manage-data/add-data