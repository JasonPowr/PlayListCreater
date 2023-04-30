package org.wit.playlistcreater.models.eventModel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EventModel(
    var id: String? = null,
    var description: String? = null,
    var date: String? = null,
    var time: String? = null,
    var type: String? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
) : Parcelable
