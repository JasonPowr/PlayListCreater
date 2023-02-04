package org.wit.playlistcreater.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.playlistcreater.adapters.SongAdapter
import org.wit.playlistcreater.databinding.FragmentSongBinding
import org.wit.playlistcreater.main.PlaylistCreater
import org.wit.playlistcreater.models.AppManager

class SongFragment : Fragment() {

    lateinit var app: PlaylistCreater
    private var _fragBinding: FragmentSongBinding? = null
    private val fragBinding get() = _fragBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as PlaylistCreater
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentSongBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        fragBinding.recyclerViewForSongs.layoutManager = LinearLayoutManager(activity)
        fragBinding.recyclerViewForSongs.adapter = SongAdapter(AppManager.findAllSongsInStore())
        return root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SongFragment().apply {
                arguments = Bundle().apply { }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}
