package org.wit.playlistcreater.ui.playlistList

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.wit.playlistcreater.R
import org.wit.playlistcreater.adapters.PlayistClickListner
import org.wit.playlistcreater.adapters.PlaylistAdapter
import org.wit.playlistcreater.databinding.FragmentPlaylistBinding
import org.wit.playlistcreater.models.AppManager
import org.wit.playlistcreater.models.playlistModel.PlaylistModel
import org.wit.playlistcreater.utils.*


class PlaylistFragment : Fragment(), PlayistClickListner {

    private val args by navArgs<PlaylistFragmentArgs>()
    private var _fragBinding: FragmentPlaylistBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var playlistViewModel: PlaylistViewModel
    lateinit var loader: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentPlaylistBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        loader = createLoader(requireActivity())

        fragBinding.recyclerViewForPlaylists.layoutManager = LinearLayoutManager(activity)
        playlistViewModel = ViewModelProvider(this)[PlaylistViewModel::class.java]

        if (!playlistViewModel.getIsLoaded() && (args.songId == "default")) {
            showLoader(loader)
        }

        setSwipeRefresh()
        playlistViewModel.observablePlaylistList.observe(viewLifecycleOwner, Observer { playlists ->
            playlists?.let {
                render(playlists as ArrayList<PlaylistModel>)
                checkSwipeRefresh()

                if (!playlistViewModel.getIsLoaded()) {
                    Handler().postDelayed({
                        hideLoader(loader)
                    }, 3000)
                }
            }
        })

        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as AppCompatActivity).findViewById<DrawerLayout>(R.id.drawer_layout)
            .setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

        val swipeDeleteHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = fragBinding.recyclerViewForPlaylists.adapter as PlaylistAdapter
                val playlistId = viewHolder.itemView.tag as Long
                playlistViewModel.swipeDelete(playlistViewModel.getPlaylist(playlistId)!!)
                adapter.removeAt(viewHolder.adapterPosition)
                if (playlistViewModel.observablePlaylistList.value?.isEmpty() == true) {
                    fragBinding.noPlaylistTxt.visibility = View.VISIBLE
                }

            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(fragBinding.recyclerViewForPlaylists)

        val swipeEditHandler = object : SwipeToEditCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val playlistId = viewHolder.itemView.tag as Long
                val action =
                    PlaylistFragmentDirections.actionPlaylistFragmentToCreatePlaylistFragment()
                        .setEdit(true).setPlaylistId(playlistId)
                findNavController().navigate(action)
            }
        }
        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(fragBinding.recyclerViewForPlaylists)

        return root;
    }

    private fun setSwipeRefresh() {
        fragBinding.swipeRefresh.setOnRefreshListener {
            playlistViewModel.load()
            fragBinding.swipeRefresh.isRefreshing = false
        }
    }

    private fun checkSwipeRefresh() {
        if (fragBinding.swipeRefresh.isRefreshing)
            fragBinding.swipeRefresh.isRefreshing = false
    }

    private fun render(playlistList: ArrayList<PlaylistModel>) {
        fragBinding.recyclerViewForPlaylists.adapter = PlaylistAdapter(playlistList, this)
        fragBinding.recyclerViewForPlaylists.layoutManager = GridLayoutManager(
            activity,
            2
        ) //https://stackoverflow.com/questions/50697791/android-recyclerview-item-side-by-sid

        if (args.songId != "default") {
            (activity as AppCompatActivity).supportActionBar?.hide() //https://stackoverflow.com/questions/26998455/how-to-get-toolbar-from-fragment
            (activity as AppCompatActivity).findViewById<DrawerLayout>(R.id.drawer_layout)
                .setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }


        fragBinding.noPlaylistTxt.visibility = View.GONE
        if (playlistList.isEmpty()) {
            fragBinding.noPlaylistTxt.visibility = View.VISIBLE
            fragBinding.recyclerViewForPlaylists.visibility = View.GONE
        } else {
            fragBinding.noPlaylistTxt.visibility = View.GONE
            fragBinding.recyclerViewForPlaylists.visibility = View.VISIBLE
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

    override fun onPlaylistClick(playlist: PlaylistModel) {
        if (args.songId == "default") {
            val action =
                PlaylistFragmentDirections.actionPlaylistFragmentToPlaylistSongViewFragment(
                    playlist.id
                )
            findNavController().navigate(action)
        } else {
            val error = playlistViewModel.addSongToPlaylist(args.songId, playlist)
            findNavController().popBackStack()
            when (error) {
                true -> {
                    Toast.makeText(
                        context,
                        AppManager.findSongByID(args.songId)?.track!!.name + " Added to Playlist: " + playlist.title,
                        Toast.LENGTH_LONG
                    ).show()
                }
                false -> Toast.makeText(
                    context,
                    "Error Adding " + AppManager.findSongByID(args.songId)?.track!!.name + " to " + playlist.title,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
