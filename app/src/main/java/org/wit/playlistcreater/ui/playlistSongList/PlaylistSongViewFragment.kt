package org.wit.playlistcreater.ui.playlistSongList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.playlistcreater.adapters.SongAdapter
import org.wit.playlistcreater.adapters.SongClickListener
import org.wit.playlistcreater.databinding.FragmentPlaylistSongViewBinding
import org.wit.playlistcreater.models.songModel.Songs

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
        playlistSongViewViewModel.observablePlaylistSongs.observe(
            viewLifecycleOwner,
            Observer { song ->
                song?.let { render(song) }
            })
        setEditPlaylistBtnListener(fragBinding)
        setDelPlaylistBtnListener(fragBinding)

        return root
    }

    private fun setEditPlaylistBtnListener(layout: FragmentPlaylistSongViewBinding) {

        layout.editBtn.setOnClickListener {
            val action =
                PlaylistSongViewFragmentDirections.actionPlaylistSongViewFragmentToCreatePlaylistFragment()
                    .setEdit(true).setPlaylistId(args.playlistId)
            findNavController().navigate(action)
        }
    }

    private fun setDelPlaylistBtnListener(layout: FragmentPlaylistSongViewBinding) {
        layout.deleteBtn.setOnClickListener {
            playlistSongViewViewModel.deletePlaylist(args.playlistId)
            findNavController().popBackStack()
        }
    }

    private fun render(songs: List<Songs?>) {
        fragBinding.recyclerViewForSongsInPlaylist.adapter = SongAdapter(songs, this)
        fragBinding.playlistTitle.text =
            playlistSongViewViewModel.getPlaylist(args.playlistId)!!.title
        if (songs.isEmpty()) {
            fragBinding.editBtn.visibility = View.GONE
            fragBinding.deleteBtn.visibility = View.GONE
            fragBinding.playlistTitle.visibility = View.GONE
            fragBinding.noSongsInPlaylist.visibility = View.VISIBLE
            fragBinding.recyclerViewForSongsInPlaylist.visibility = View.GONE
        } else {
            fragBinding.editBtn.visibility = View.VISIBLE
            fragBinding.deleteBtn.visibility = View.VISIBLE
            fragBinding.playlistTitle.visibility = View.VISIBLE
            fragBinding.recyclerViewForSongsInPlaylist.visibility = View.VISIBLE

            fragBinding.noSongsInPlaylist.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onSongClick(songs: Songs?) {
        val action =
            PlaylistSongViewFragmentDirections.actionPlaylistSongViewFragmentToSongInfoFragment(
                songs!!.track.id
            ).setCameFromPlaylist(true).setPlaylistId(args.playlistId)
        findNavController().navigate(action)
    }

}