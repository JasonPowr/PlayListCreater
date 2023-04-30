package org.wit.playlistcreater.firebaseui

import android.content.Intent
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import org.wit.playlistcreater.R

object FirebaseUIAuthManager {

    lateinit var auth: FirebaseAuth

    fun isSignedIn(): Boolean {
        auth = FirebaseAuth.getInstance()
        return auth.currentUser != null
    }

    fun createAndLaunchSignInIntent(layout: Int): Intent {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        val customLayout = AuthMethodPickerLayout
            .Builder(layout)
            .setGoogleButtonId(R.id.googleSignInButton)
            .setEmailButtonId(R.id.emailSignInButton)
            .build()

        return AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setIsSmartLockEnabled(false, true)
            .setTheme(R.style.Theme_PlaylistCreater)
            .setLogo(R.drawable.musiclogo)
            .setAuthMethodPickerLayout(customLayout)
            .build()
    }
}