package org.wit.playlistcreater.ui.songList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.playlistcreater.adapters.SongAdapter
import org.wit.playlistcreater.databinding.FragmentSongBinding
import org.wit.playlistcreater.models.songModel.Song

class SongFragment : Fragment() {

    private var _fragBinding: FragmentSongBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var songViewModel: SongViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentSongBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        fragBinding.recyclerViewForSongs.layoutManager = LinearLayoutManager(activity)
        songViewModel = ViewModelProvider(this)[SongViewModel::class.java]
        songViewModel.observableSongsList.observe(viewLifecycleOwner, Observer {
                songs ->
            songs?.let { render(songs) }
        })

        return root
    }

    private fun render(songList: List<Song?>) {
        fragBinding.recyclerViewForSongs.adapter = SongAdapter(songList)
        if (songList.isEmpty()) {
            fragBinding.recyclerViewForSongs.visibility = View.GONE
            fragBinding.loading.visibility = View.VISIBLE
            fragBinding.loadingSymbol.visibility = View.VISIBLE
        } else {
            fragBinding.recyclerViewForSongs.visibility = View.VISIBLE
            fragBinding.loading.visibility = View.GONE
            fragBinding.loadingSymbol.visibility = View.GONE
        }
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
