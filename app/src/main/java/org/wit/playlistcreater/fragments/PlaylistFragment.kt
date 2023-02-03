package org.wit.playlistcreater.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import org.wit.playlistcreater.databinding.FragmentPlaylistBinding
import org.wit.playlistcreater.main.PlaylistCreater

class PlaylistFragment : Fragment() {
    lateinit var app: PlaylistCreater
    private var _fragBinding: FragmentPlaylistBinding? = null
    private val fragBinding get() = _fragBinding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        app = activity?.application as PlaylistCreater
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return fragBinding.root;
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onResume() {
        super.onResume()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PlaylistFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}
