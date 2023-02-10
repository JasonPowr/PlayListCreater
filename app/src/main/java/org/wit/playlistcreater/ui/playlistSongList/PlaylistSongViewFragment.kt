package org.wit.playlistcreater.ui.playlistSongList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.playlistcreater.adapters.SongAdapter
import org.wit.playlistcreater.adapters.SongClickListener
import org.wit.playlistcreater.databinding.FragmentPlaylistSongViewBinding
import org.wit.playlistcreater.models.AppManager
import org.wit.playlistcreater.models.songModel.Song

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
        setEditBtnListener(fragBinding)
        setDelBtnListener(fragBinding)

        return root
    }

    private fun setEditBtnListener(layout: FragmentPlaylistSongViewBinding){

        layout.editBtn.setOnClickListener {
            val action = PlaylistSongViewFragmentDirections.actionPlaylistSongViewFragmentToCreatePlaylistFragment().setEdit(true).setPlaylistId(args.playlistId)
            findNavController().navigate(action)
            }
        }

    private fun setDelBtnListener(layout: FragmentPlaylistSongViewBinding) {
        layout.deleteBtn.setOnClickListener {

        }
    }

    private fun render(songList: List<Song?>) {
        fragBinding.recyclerViewForSongsInPlaylist.adapter = SongAdapter(songList, this)
        fragBinding.playlistTitle.text = playlistSongViewViewModel.getPlaylist(args.playlistId)!!.title
        if (songList.isEmpty()) {
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

    override fun onSongClick(song: Song?) {
        val action = PlaylistSongViewFragmentDirections.actionPlaylistSongViewFragmentToSongInfoFragment(
            song!!.track.id)
        findNavController().navigate(action)
    }

}