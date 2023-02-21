package org.wit.playlistcreater.ui.loginOrRegister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.wit.playlistcreater.R
import org.wit.playlistcreater.databinding.FragmentLoginBinding

class LoginOrRegisterFragment : Fragment() {

    private var _fragBinding: FragmentLoginBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var loginOrRegisterViewModel: LoginOrRegisterViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val action =
                LoginOrRegisterFragmentDirections.actionLoginFragmentToPlaylistFragment()
                    .setUserId(
                        currentUser.uid
                    )
            findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentLoginBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        loginOrRegisterViewModel = ViewModelProvider(this)[LoginOrRegisterViewModel::class.java]

        (activity as AppCompatActivity).supportActionBar?.hide() //https://stackoverflow.com/questions/26998455/how-to-get-toolbar-from-fragment
        (activity as AppCompatActivity).findViewById<DrawerLayout>(R.id.drawer_layout)
            .setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        setRegisterBtnListener(fragBinding)
        setLoginBtnListener(fragBinding)

        return root
    }

    private fun setRegisterBtnListener(layout: FragmentLoginBinding) {
        layout.registerBtn.setOnClickListener {

            val email = layout.userEmail.text.toString()
            val password = layout.userPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) { task -> //https://stackoverflow.com/questions/65817683/android-kotlin-firebase-addoncompletelistener-showing-error
                        if (task.isSuccessful) {
                            loginOrRegisterViewModel.createUser(
                                auth.currentUser!!.uid,
                                auth.currentUser!!.email.toString()
                            )
                            val navigationView =
                                (activity as AppCompatActivity).findViewById<NavigationView>(R.id.nav_view)
                            val header = navigationView.getHeaderView(0)
                            val userEmail = header.findViewById<View>(R.id.userEmail) as TextView
                            userEmail.text = auth.currentUser!!.email
                            //https://stackoverflow.com/questions/38987012/how-to-programmatically-change-the-text-and-image-of-the-navigation-header-for-a

                            val action =
                                LoginOrRegisterFragmentDirections.actionLoginFragmentToPlaylistFragment()
                                    .setUserId(
                                        auth.currentUser!!.uid
                                    )
                            findNavController().navigate(action)
                        } else {
                            Toast.makeText(context, "Registration Failed", Toast.LENGTH_LONG).show()
                        }
                    }
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
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            val navigationView =
                                (activity as AppCompatActivity).findViewById<NavigationView>(R.id.nav_view)
                            val header = navigationView.getHeaderView(0)
                            val userEmail = header.findViewById<View>(R.id.userEmail) as TextView
                            userEmail.text = auth.currentUser!!.email

                            val user = auth.currentUser
                            val action =
                                LoginOrRegisterFragmentDirections.actionLoginFragmentToPlaylistFragment()
                                    .setUserId(
                                        user!!.uid
                                    )
                            findNavController().navigate(action)
                        } else {
                            Toast.makeText(context, "Login Failed", Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                Toast.makeText(context, "Please fill in both fields", Toast.LENGTH_LONG).show()
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

}