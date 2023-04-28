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

        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        // Set custom layout login options
        val customLayout = AuthMethodPickerLayout
            .Builder(layout)
            .setGoogleButtonId(R.id.googleSignInButton)
            .setEmailButtonId(R.id.emailSignInButton)
            .setPhoneButtonId(R.id.phoneSignInButton)
            .build()

        return AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            // true,true for Smart Lock
            .setIsSmartLockEnabled(false, true)
            .setTheme(R.style.Theme_PlaylistCreater)
            .setAuthMethodPickerLayout(customLayout)
            .build()
    }
}