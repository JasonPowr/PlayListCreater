package org.wit.playlistcreater.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import org.wit.playlistcreater.firebase.FirebaseAuthManager
import org.wit.playlistcreater.models.AppManager

class LoginOrRegisterViewModel(app: Application) : AndroidViewModel(app) {

    var firebaseAuthManager: FirebaseAuthManager = FirebaseAuthManager(app)
    var liveFirebaseUser: MutableLiveData<FirebaseUser> = firebaseAuthManager.liveFirebaseUser

    fun createUser(uid: String, email: String) {
        AppManager.createUser(uid, email)
    }

    fun login(email: String?, password: String?) {
        firebaseAuthManager.login(email, password)
    }

    fun register(email: String?, password: String?) {
        firebaseAuthManager.register(email, password)
    }

}