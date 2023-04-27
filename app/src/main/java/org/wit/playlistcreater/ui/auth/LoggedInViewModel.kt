package org.wit.playlistcreater.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import org.wit.playlistcreater.firebase.FirebaseAuthManager
import org.wit.playlistcreater.models.AppManager

class LoggedInViewModel(app: Application) : AndroidViewModel(app) {

    var firebaseAuthManager: FirebaseAuthManager = FirebaseAuthManager(app)
    var liveFirebaseUser: MutableLiveData<FirebaseUser> = firebaseAuthManager.liveFirebaseUser
    var loggedOut: MutableLiveData<Boolean> = firebaseAuthManager.loggedOut

    fun logOut() {
        AppManager.deleteAll()
        firebaseAuthManager.logOut()
    }
}