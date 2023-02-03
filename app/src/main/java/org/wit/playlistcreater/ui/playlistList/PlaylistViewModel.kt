package org.wit.playlistcreater.ui.playlistList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlaylistViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status
//
//    fun addDonation(donation: DonationModel) {
//        status.value = try {
//            DonationManager.create(donation)
//            true
//        } catch (e: IllegalArgumentException) {
//            false
//        }
//    }
}