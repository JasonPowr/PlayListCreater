package org.wit.playlistcreater.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import org.wit.playlistcreater.databinding.FragmentProfileBinding
import org.wit.playlistcreater.models.AppManager.auth

class ProfileFragment : Fragment() {

    private var _fragBinding: FragmentProfileBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var profileViewModel: ProfileViewModel

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

        val currentUser = auth.currentUser
        fragBinding.userEmail.text = currentUser!!.email
        setLogoutBtnListener(fragBinding)
        
        return root;
    }

    private fun setLogoutBtnListener(layout: FragmentProfileBinding) {
        layout.logoutBtn.setOnClickListener {
            auth.signOut()
            profileViewModel.removeAllFromMem()
            findNavController().popBackStack()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

}