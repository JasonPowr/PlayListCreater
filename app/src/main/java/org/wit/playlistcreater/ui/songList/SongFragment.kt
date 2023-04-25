package org.wit.playlistcreater.ui.songList

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.playlistcreater.adapters.SongAdapter
import org.wit.playlistcreater.adapters.SongClickListener
import org.wit.playlistcreater.databinding.FragmentSongBinding
import org.wit.playlistcreater.models.songModel.Songs
import org.wit.playlistcreater.utils.createLoader
import org.wit.playlistcreater.utils.hideLoader
import org.wit.playlistcreater.utils.showLoader

class SongFragment : Fragment(), SongClickListener {

    private var _fragBinding: FragmentSongBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var songViewModel: SongViewModel
    lateinit var loader: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentSongBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        loader = createLoader(requireActivity())


        fragBinding.recyclerViewForSongs.layoutManager = LinearLayoutManager(activity)
        songViewModel = ViewModelProvider(this)[SongViewModel::class.java]

        showLoader(loader)
        songViewModel.observableSongs.observe(viewLifecycleOwner, Observer { songs ->
            songs?.let {
                render(songs)
                hideLoader(loader)
            }
        })
        return root
    }

    private fun render(songs: List<Songs?>) {
        fragBinding.recyclerViewForSongs.adapter = SongAdapter(songs, this)
    }

    override fun onSongClick(songs: Songs?) {
        val action = SongFragmentDirections.actionSongFragmentToSongInfoFragment(songs!!.track.id)
        findNavController().navigate(action)
    }

    override fun onResume() {
        super.onResume()
        songViewModel.load()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

}
