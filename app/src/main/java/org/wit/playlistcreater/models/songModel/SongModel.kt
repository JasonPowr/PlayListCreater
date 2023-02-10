package org.wit.playlistcreater.models.songModel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SongModel(
    val items: List<Songs>,
) : Parcelable

//https://rapidapi.com/Glavier/api/spotify23/
//https://stackoverflow.com/questions/36177629/retrofit2-android-expected-begin-array-but-was-begin-object-at-line-1-column-2
//https://www.youtube.com/watch?v=-n50LDUT5w8&t=20s