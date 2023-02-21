package org.wit.playlistcreater.ui.loginOrRegister

import androidx.lifecycle.ViewModel
import org.wit.playlistcreater.models.AppManager

class LoginOrRegisterViewModel : ViewModel() {

    fun createUser(uid: String, email: String) {
        AppManager.createUser(uid, email)
    }

}