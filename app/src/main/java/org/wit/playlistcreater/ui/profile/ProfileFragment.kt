package org.wit.playlistcreater.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.playlistcreater.R
import org.wit.playlistcreater.adapters.PublicPlaylistAdapter
import org.wit.playlistcreater.adapters.PublicPlaylistClickListener
import org.wit.playlistcreater.databinding.FragmentProfileBinding
import org.wit.playlistcreater.firebase.FirebaseImageManager
import org.wit.playlistcreater.models.publicPlaylistModel.PublicPlaylistModel
import org.wit.playlistcreater.utils.readImageUri
import org.wit.playlistcreater.utils.showImagePicker

class ProfileFragment : Fragment(), PublicPlaylistClickListener {

    private var _fragBinding: FragmentProfileBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var intentLauncher: ActivityResultLauncher<Intent>

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
        fragBinding.recyclerViewForPlaylists.layoutManager = LinearLayoutManager(activity)
        profileViewModel.load()
        profileViewModel.observableLikedPlaylistList.observe(viewLifecycleOwner,
            Observer { likedPlaylist ->
                render(likedPlaylist as ArrayList<PublicPlaylistModel>)
            })

        fragBinding.userProfileImage.setOnClickListener {
            showImagePicker(intentLauncher)
        }
        registerImagePickerCallback()

        fragBinding.userEmail.text = profileViewModel.currentUser!!.email
        FirebaseImageManager.imageUri.observe(viewLifecycleOwner) { result ->
            if (result == Uri.EMPTY) {
                if (profileViewModel.currentUser!!.photoUrl != null) {
                    FirebaseImageManager.updateUserImage(
                        profileViewModel.currentUser!!.uid,
                        profileViewModel.currentUser!!.photoUrl,
                        fragBinding.userProfileImage,
                        false
                    )

                } else {
                    FirebaseImageManager.updateDefaultImage(
                        profileViewModel.currentUser!!.uid,
                        R.drawable.ic_baseline_library_music_24,
                        fragBinding.userProfileImage,
                    )
                }
            } else {
                FirebaseImageManager.updateUserImage(
                    profileViewModel.currentUser!!.uid,
                    FirebaseImageManager.imageUri.value,
                    fragBinding.userProfileImage, false
                )
            }
        }

        fragBinding.userName.text = profileViewModel.currentUser!!.displayName

        return root;
    }

    private fun registerImagePickerCallback() {
        intentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            FirebaseImageManager
                                .updateUserImage(
                                    profileViewModel.currentUser!!.uid,
                                    readImageUri(result.resultCode, result.data),
                                    fragBinding.userProfileImage,
                                    true
                                )
                        }
                    }
                    AppCompatActivity.RESULT_CANCELED -> {}
                    else -> {}
                }
            }
    }


    private fun render(publicPlaylist: ArrayList<PublicPlaylistModel>) {
        fragBinding.recyclerViewForPlaylists.adapter = PublicPlaylistAdapter(publicPlaylist, this)
    }


    override fun onResume() {
        super.onResume()
        profileViewModel.load()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onPublicPlaylistClick(publicPlaylist: PublicPlaylistModel) {
        val action =
            ProfileFragmentDirections.actionProfileFragmentToPublicPlaylistViewFragment(
                publicPlaylist.playlist!!.publicID.toString()
            )
        findNavController().navigate(action)
    }

}