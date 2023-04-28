package org.wit.playlistcreater.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import org.wit.playlistcreater.models.AppManager

class LoggedInViewModel(app: Application) : AndroidViewModel(app) {

    var liveFirebaseUser = MutableLiveData<FirebaseUser>()
    var loggedOut = MutableLiveData<Boolean>()
    var errorStatus = MutableLiveData<Boolean>()

    fun signOut() {
        AppManager.deleteAll()
        loggedOut.postValue(true)
        errorStatus.postValue(false)
    }
}
