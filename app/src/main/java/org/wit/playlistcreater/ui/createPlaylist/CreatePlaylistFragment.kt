package org.wit.playlistcreater.ui.createPlaylist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import org.wit.playlistcreater.R
import org.wit.playlistcreater.databinding.FragmentCreatePlaylistBinding
import org.wit.playlistcreater.models.playlistModel.PlaylistModel

class CreatePlaylistFragment : Fragment() {

    private var _fragBinding: FragmentCreatePlaylistBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var createPlaylistViewModel: CreatePlaylistViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _fragBinding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        createPlaylistViewModel = ViewModelProvider(this)[CreatePlaylistViewModel::class.java]
        setButtonListener(fragBinding)
        return root;
    }

    fun setButtonListener(layout: FragmentCreatePlaylistBinding) {
        layout.createPlaylistBtn.setOnClickListener {
            if(layout.editTextTextPersonName.text.isNotEmpty()){
                val playlistTitle = layout.editTextTextPersonName.text.toString()
                createPlaylistViewModel.createPlaylist(PlaylistModel(0,playlistTitle, mutableListOf()))
                findNavController().popBackStack()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

}