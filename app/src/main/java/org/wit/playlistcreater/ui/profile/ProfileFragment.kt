package org.wit.playlistcreater.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.wit.playlistcreater.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _fragBinding: FragmentProfileBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentProfileBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        auth = Firebase.auth

        val currentUser = auth.currentUser

        setLogoutBtnListener(fragBinding)
        return root;
    }

    private fun setLogoutBtnListener(layout: FragmentProfileBinding) {
        layout.logoutBtn.setOnClickListener {
            Firebase.auth.signOut()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

}