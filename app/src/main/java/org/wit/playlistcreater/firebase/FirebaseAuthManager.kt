package org.wit.playlistcreater.firebase

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseAuthManager(application: Application) {

    private var application: Application? = null

    var firebaseAuth: FirebaseAuth? = null
    var liveFirebaseUser = MutableLiveData<FirebaseUser>()
    var loggedOut = MutableLiveData<Boolean>()
    var errorStatus = MutableLiveData<Boolean>()

    init {
        this.application = application
        firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth!!.currentUser != null) {
            liveFirebaseUser.postValue(firebaseAuth!!.currentUser)
            loggedOut.postValue(false)
            errorStatus.postValue(false)
        }
    }

    fun login(email: String?, password: String?) {
        firebaseAuth!!.signInWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(application!!.mainExecutor) { task ->
                if (task.isSuccessful) {
                    liveFirebaseUser.postValue(firebaseAuth!!.currentUser)
                    errorStatus.postValue(false)
                } else {
                    errorStatus.postValue(true)
                }
            }
    }

    fun register(email: String?, password: String?) {
        firebaseAuth!!.createUserWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(application!!.mainExecutor) { task ->
                if (task.isSuccessful) {
                    liveFirebaseUser.postValue(firebaseAuth!!.currentUser)
                    errorStatus.postValue(false)
                } else {
                    errorStatus.postValue(true)
                }
            }
    }

    fun logOut() {
        firebaseAuth!!.signOut()
        loggedOut.postValue(true)
        errorStatus.postValue(false)
    }
}