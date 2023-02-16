package org.wit.playlistcreater.ui.createPlaylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import org.wit.playlistcreater.databinding.FragmentCreatePlaylistBinding
import org.wit.playlistcreater.models.playlistModel.PlaylistModel

class CreatePlaylistFragment : Fragment() {

    private val args by navArgs<CreatePlaylistFragmentArgs>()
    private var _fragBinding: FragmentCreatePlaylistBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var createPlaylistViewModel: CreatePlaylistViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        createPlaylistViewModel = ViewModelProvider(this)[CreatePlaylistViewModel::class.java]
        setButtonListener(fragBinding)
        return root;
    }

    fun playlistAlreadyExists(playlistName: String): Boolean {
        val playlists = createPlaylistViewModel.returnAllPlaylists()

        for (playlist in playlists) {
            if (playlist.title == playlistName) {
                return true
            }
        }
        return false
    }

    fun setButtonListener(layout: FragmentCreatePlaylistBinding) {
        layout.createPlaylistBtn.setOnClickListener {
            val playlistTitle = layout.editTextTextPersonName.text.toString()
            val updatedPlaylist = PlaylistModel(0, playlistTitle, mutableListOf())

            if (layout.editTextTextPersonName.text.isNotEmpty()) {
                if (args.edit && (args.playlistId != -1L)) {
                    createPlaylistViewModel.updatePlaylist(args.playlistId, updatedPlaylist)
                } else {
                    if (!playlistAlreadyExists(playlistTitle)) {
                        createPlaylistViewModel.createPlaylist(
                            PlaylistModel(
                                0,
                                playlistTitle,
                                mutableListOf()
                            )
                        )
                    } else {
                        Toast.makeText(
                            context, "Playlist already Exists", Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

}