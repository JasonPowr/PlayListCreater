package org.wit.playlistcreater.ui.playlistList

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.playlistcreater.adapters.PlaylistAdapter
import org.wit.playlistcreater.databinding.FragmentPlaylistBinding
import org.wit.playlistcreater.models.playlistModel.PlaylistModel

class PlaylistFragment : Fragment() {

    private var _fragBinding: FragmentPlaylistBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var playlistViewModel: PlaylistViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
        _fragBinding = FragmentPlaylistBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        fragBinding.recyclerViewForPlaylists.layoutManager = LinearLayoutManager(activity)
        playlistViewModel = ViewModelProvider(this)[PlaylistViewModel::class.java]
        playlistViewModel.observablePlaylistList.observe(viewLifecycleOwner, Observer {
                songs ->
            songs?.let { render(songs) }
        })

        return root;
    }

    private fun render(playlistList: List<PlaylistModel>) {
        fragBinding.recyclerViewForPlaylists.adapter = PlaylistAdapter(playlistList)
        if (playlistList.isEmpty()) {
            fragBinding.recyclerViewForPlaylists.visibility = View.GONE
            fragBinding.noPlaylistTxt.visibility = View.VISIBLE
        } else {
            fragBinding.recyclerViewForPlaylists.visibility = View.VISIBLE
            fragBinding.noPlaylistTxt.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        playlistViewModel.load()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}
