package org.wit.playlistcreater.ui.songList

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.playlistcreater.adapters.SongAdapter
import org.wit.playlistcreater.adapters.SongClickListener
import org.wit.playlistcreater.databinding.FragmentSongBinding
import org.wit.playlistcreater.models.songModel.Songs
import org.wit.playlistcreater.utils.createLoader
import org.wit.playlistcreater.utils.hideLoader
import org.wit.playlistcreater.utils.showLoader
import java.util.*


class SongFragment : Fragment(), SongClickListener {

    private val args by navArgs<SongFragmentArgs>()
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

        when (args.context) {
            "option1" -> {
                songViewModel.observableSongsOption1.observe(viewLifecycleOwner, Observer { songs ->
                    songs?.let {
                        render(songs as ArrayList<Songs?>)
                        hideLoader(loader)
                    }
                })
            }
            "option2" -> {
                fragBinding.titleTxt.text = "Spotify's Top 50"
                songViewModel.observableSongsOption2.observe(viewLifecycleOwner, Observer { songs ->
                    songs?.let {
                        render(songs as ArrayList<Songs?>)
                        hideLoader(loader)
                    }
                })

            }
            "option3" -> {
                fragBinding.titleTxt.text = "New Releases"
                songViewModel.observableSongsOption3.observe(viewLifecycleOwner, Observer { songs ->
                    songs?.let {
                        render(songs as ArrayList<Songs?>)
                        hideLoader(loader)
                    }
                })
            }
        }

        handleSearch(fragBinding)
        setSwipeRefresh()
        return root
    }

    private fun handleSearch(fragBinding: FragmentSongBinding) {
        fragBinding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filter(newText)
                return false
            }
        })
    }

    private fun filter(text: String) {
        val filteredList: ArrayList<Songs?> = ArrayList<Songs?>()
        for (songs in songViewModel.observableSongsOption1.value!!) {
            if (songs != null) {
                if (songs.track!!.name!!.lowercase(Locale.ROOT)
                        .contains(text.lowercase(Locale.getDefault()))
                ) {
                    filteredList.add(songs)
                }
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(context, "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            fragBinding.recyclerViewForSongs.adapter = SongAdapter(filteredList, this)
        }
    }
    //https://www.geeksforgeeks.org/searchview-in-android-with-recyclerview/

    private fun setSwipeRefresh() {
        fragBinding.swipeRefresh.setOnRefreshListener {
            showLoader(loader)
            songViewModel.load()
            fragBinding.swipeRefresh.isRefreshing = false
        }
    }


    private fun render(songs: ArrayList<Songs?>) {
        fragBinding.recyclerViewForSongs.adapter = SongAdapter(songs, this)
    }

    override fun onSongClick(songs: Songs?) {

        when (args.context) {
            "option1" -> {
                val action =
                    SongFragmentDirections.actionSongFragmentToSongInfoFragment(songs!!.track!!.id.toString())
                        .setContext("irelandTop50")
                findNavController().navigate(action)
            }
            "option2" -> {
                val action =
                    SongFragmentDirections.actionSongFragmentToSongInfoFragment(songs!!.track!!.id.toString())
                        .setContext("spotify50")
                findNavController().navigate(action)
            }
            "option3" -> {
                val action =
                    SongFragmentDirections.actionSongFragmentToSongInfoFragment(songs!!.track!!.id.toString())
                        .setContext("newReleases")
                findNavController().navigate(action)
            }
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
