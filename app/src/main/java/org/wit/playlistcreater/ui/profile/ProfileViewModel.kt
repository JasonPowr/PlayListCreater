package org.wit.playlistcreater.ui.profile

import androidx.lifecycle.ViewModel
import org.wit.playlistcreater.models.AppManager

class ProfileViewModel : ViewModel() {

    fun removeAllFromMem() {
        AppManager.deleteAll()
    }
}