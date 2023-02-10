package org.wit.playlistcreater.ui.playlistSongList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.playlistcreater.adapters.PlayistClickListner
import org.wit.playlistcreater.adapters.SongAdapter
import org.wit.playlistcreater.adapters.SongClickListener
import org.wit.playlistcreater.databinding.FragmentPlaylistSongViewBinding
import org.wit.playlistcreater.models.playlistModel.PlaylistModel
import org.wit.playlistcreater.models.songModel.Song
import org.wit.playlistcreater.ui.songInfo.SongInfoFragmentArgs

class PlaylistSongViewFragment : Fragment(), SongClickListener {

    private val args by navArgs<PlaylistSongViewFragmentArgs>()
    private var _fragBinding: FragmentPlaylistSongViewBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var playlistSongViewViewModel: PlaylistSongViewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentPlaylistSongViewBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        fragBinding.recyclerViewForSongsInPlaylist.layoutManager = LinearLayoutManager(activity)
        playlistSongViewViewModel = ViewModelProvider(this)[PlaylistSongViewViewModel::class.java]
        playlistSongViewViewModel.getSongsInPlaylist(args.playlistId)
        playlistSongViewViewModel.observablePlaylistSongList.observe(viewLifecycleOwner, Observer {
                song ->
            song?.let { render(song) }
        })
        return root
    }

    private fun render(songList: List<Song?>) {
        fragBinding.recyclerViewForSongsInPlaylist.adapter = SongAdapter(songList, this)
        if (songList.isEmpty()) {
            fragBinding.recyclerViewForSongsInPlaylist.visibility = View.GONE
            fragBinding.noSongsInPlaylist.visibility = View.VISIBLE
        } else {
            fragBinding.recyclerViewForSongsInPlaylist.visibility = View.VISIBLE
            fragBinding.noSongsInPlaylist.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onSongClick(song: Song?) {
        TODO("Not yet implemented")
    }

}