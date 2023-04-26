package org.wit.playlistcreater.ui.auth

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import org.wit.playlistcreater.R
import org.wit.playlistcreater.databinding.FragmentLoginBinding

class LoginOrRegisterFragment : Fragment() {

    private var _fragBinding: FragmentLoginBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var loginOrRegisterViewModel: LoginOrRegisterViewModel
    private lateinit var startForResult: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentLoginBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        loginOrRegisterViewModel = ViewModelProvider(this)[LoginOrRegisterViewModel::class.java]

        loginOrRegisterViewModel.liveFirebaseUser.observe(viewLifecycleOwner, Observer
        { firebaseUser ->
            if (firebaseUser != null) {
                val action =
                    LoginOrRegisterFragmentDirections.actionLoginFragmentToPlaylistFragment()
                findNavController().navigate(action)
            }

        })
        loginOrRegisterViewModel.firebaseAuthManager.errorStatus.observe(viewLifecycleOwner,
            Observer
            { status -> checkStatus(status) })

        (activity as AppCompatActivity).supportActionBar?.hide() //https://stackoverflow.com/questions/26998455/how-to-get-toolbar-from-fragment
        (activity as AppCompatActivity).findViewById<DrawerLayout>(R.id.drawer_layout)
            .setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        setRegisterBtnListener(fragBinding)
        setLoginBtnListener(fragBinding)
        setGoogleSignInBtn(fragBinding)
        setupGoogleSignInCallback()
        return root
    }

    private fun googleSignIn() {
        val signInIntent = loginOrRegisterViewModel.firebaseAuthManager
            .googleSignInClient.value!!.signInIntent

        startForResult.launch(signInIntent)
    }

    private fun setupGoogleSignInCallback() {
        startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                        try {
                            // Google Sign In was successful, authenticate with Firebase
                            val account = task.getResult(ApiException::class.java)
                            loginOrRegisterViewModel.authWithGoogle(account!!)
                        } catch (e: ApiException) {
                            // Google Sign In failed
                            Snackbar.make(
                                fragBinding.frameLayout4, "Authentication Failed.",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                    RESULT_CANCELED -> {

                    }
                    else -> {}
                }
            }
    }


    private fun setGoogleSignInBtn(layout: FragmentLoginBinding) {
        layout.googleSignInButton.setColorScheme(0)
        layout.googleSignInButton.setOnClickListener {
            googleSignIn()
        }
    }

    private fun setRegisterBtnListener(layout: FragmentLoginBinding) {
        layout.registerBtn.setOnClickListener {

            val email = layout.userEmail.text.toString()
            val password = layout.userPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginOrRegisterViewModel.register(email, password)
            } else {
                Toast.makeText(context, "Please fill in both fields", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun setLoginBtnListener(layout: FragmentLoginBinding) {
        layout.loginBtn.setOnClickListener {

            val email = layout.userEmail.text.toString()
            val password = layout.userPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginOrRegisterViewModel.login(email, password)
            } else {
                Toast.makeText(context, "Please fill in both fields", Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun checkStatus(error: Boolean) {
        if (error)
            Toast.makeText(context, "Auth Failed", Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

}