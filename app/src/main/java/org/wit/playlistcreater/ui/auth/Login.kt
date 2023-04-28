package org.wit.playlistcreater.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import org.wit.playlistcreater.R
import org.wit.playlistcreater.firebaseui.FirebaseUIAuthManager
import org.wit.playlistcreater.ui.home.Home
import timber.log.Timber.Forest.i

class Login : AppCompatActivity() {

    private lateinit var signIn: ActivityResultLauncher<Intent>

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerFirebaseAuthUICallback()
        if (FirebaseUIAuthManager.isSignedIn())
            startActivity(Intent(this, Home::class.java))
        else
            signIn.launch(
                FirebaseUIAuthManager
                    .createAndLaunchSignInIntent(R.layout.fragment_login)
            )
    }

    private fun registerFirebaseAuthUICallback() {
        signIn = registerForActivityResult(
            FirebaseAuthUIActivityResultContract(),
            this::onSignInResult
        )
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        i(" DX onSignInResult %s", result.resultCode)
        if (result.resultCode == RESULT_OK) {
            i("DX Sign in successful!")
            startActivity(Intent(this, Home::class.java))
        } else if (result.resultCode == 0) finish()
        else if (result.idpResponse == null) {
            i(" DX User Pressed Back Button")

            //Required to exit app - must investigate this further
            Toast.makeText(
                this, "Click again to Close App...",
                Toast.LENGTH_LONG
            ).show()
        } else i(result.idpResponse!!.error)
    }
    
    override fun onBackPressed() {
        Toast.makeText(this, "Click again to Close App...", Toast.LENGTH_LONG).show()
        finish()
        super.onBackPressed()
    }
}