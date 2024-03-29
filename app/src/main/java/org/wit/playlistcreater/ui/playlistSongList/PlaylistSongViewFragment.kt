package org.wit.playlistcreater.ui.playlistSongList

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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.wit.playlistcreater.adapters.SongAdapter
import org.wit.playlistcreater.adapters.SongClickListener
import org.wit.playlistcreater.databinding.FragmentPlaylistSongViewBinding
import org.wit.playlistcreater.models.songModel.Songs
import org.wit.playlistcreater.utils.SwipeToDeleteCallback
import java.util.*

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
                song?.let { render(song as ArrayList<Songs?>) }
            })
        setEditPlaylistBtnListener(fragBinding)
        setDelPlaylistBtnListener(fragBinding)


        val swipeDeleteHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = fragBinding.recyclerViewForSongsInPlaylist.adapter as SongAdapter
                val songId = viewHolder.itemView.tag as String
                adapter.removeAt(viewHolder.adapterPosition)

                playlistSongViewViewModel.swipeDelete(
                    songId,
                    playlistSongViewViewModel.getPlaylist(args.playlistId)!!
                )

                if (playlistSongViewViewModel.observablePlaylistSongs.value?.isEmpty() == true) {
                    fragBinding.noSongsInPlaylist.visibility = View.VISIBLE
                }


            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(fragBinding.recyclerViewForSongsInPlaylist)
        handleSearch(fragBinding)

        return root
    }

    private fun handleSearch(fragBinding: FragmentPlaylistSongViewBinding) {
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
        for (songs in playlistSongViewViewModel.observablePlaylistSongs.value!!) {
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
            fragBinding.recyclerViewForSongsInPlaylist.adapter = SongAdapter(filteredList, this)
        }
    }
    //https://www.geeksforgeeks.org/searchview-in-android-with-recyclerview/

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
            playlistSongViewViewModel.deletePlaylist(playlistSongViewViewModel.getPlaylist(args.playlistId)!!)
            findNavController().popBackStack()
        }
    }

    private fun render(songs: ArrayList<Songs?>) {
        fragBinding.recyclerViewForSongsInPlaylist.adapter = SongAdapter(songs, this)
        fragBinding.playlistTitle.text =
            playlistSongViewViewModel.getPlaylist(args.playlistId)!!.title
        fragBinding.genre.text =
            playlistSongViewViewModel.getPlaylist(args.playlistId)!!.playListGenre
        fragBinding.count.text =
            playlistSongViewViewModel.observablePlaylistSongs.value!!.size.toString()

        if (songs.isEmpty()) {
            fragBinding.noSongsInPlaylist.visibility = View.VISIBLE
            fragBinding.recyclerViewForSongsInPlaylist.visibility = View.GONE
            fragBinding.toggleShare.visibility = View.GONE
        } else {
            fragBinding.recyclerViewForSongsInPlaylist.visibility = View.VISIBLE
            fragBinding.noSongsInPlaylist.visibility = View.GONE
            fragBinding.toggleShare.visibility = View.VISIBLE
        }

        fragBinding.toggleShare.isChecked =
            playlistSongViewViewModel.getPlaylist(args.playlistId)!!.publicID != ""
        fragBinding.toggleShare.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) playlistSongViewViewModel.sharePlaylist(
                playlistSongViewViewModel.getPlaylist(
                    args.playlistId
                )!!
            )
            else playlistSongViewViewModel.stopShare(playlistSongViewViewModel.getPlaylist(args.playlistId)!!)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onSongClick(songs: Songs?) {
        val action =
            PlaylistSongViewFragmentDirections.actionPlaylistSongViewFragmentToSongInfoFragment(
                songs!!.track!!.id.toString()
            ).setCameFromPlaylist(true).setPlaylistId(args.playlistId)
        findNavController().navigate(action)
    }

}