package org.wit.playlistcreater.ui.publicPlaylists

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.playlistcreater.R
import org.wit.playlistcreater.adapters.PublicPlaylistAdapter
import org.wit.playlistcreater.adapters.PublicPlaylistClickListener
import org.wit.playlistcreater.databinding.FragmentPublicPlaylistsBinding
import org.wit.playlistcreater.models.publicPlaylistModel.PublicPlaylistModel
import org.wit.playlistcreater.utils.createLoader
import org.wit.playlistcreater.utils.hideLoader
import org.wit.playlistcreater.utils.showLoader


class PublicPlaylistsFragment : Fragment(), PublicPlaylistClickListener {

    private var _fragBinding: FragmentPublicPlaylistsBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var publicPlaylistsViewModel: PublicPlaylistsViewModel
    lateinit var loader: AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentPublicPlaylistsBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        loader = createLoader(requireActivity())

        fragBinding.recyclerViewForPlaylists.layoutManager = LinearLayoutManager(activity)
        publicPlaylistsViewModel = ViewModelProvider(this)[PublicPlaylistsViewModel::class.java]

        setSwipeRefresh()
        publicPlaylistsViewModel.observablePlaylistList.observe(
            viewLifecycleOwner,
            Observer { publicPlaylist ->
                publicPlaylist?.let {
                    render(publicPlaylist as ArrayList<PublicPlaylistModel>)
                    checkSwipeRefresh()
                    hideLoader(loader)
                }
            })
        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as AppCompatActivity).findViewById<DrawerLayout>(R.id.drawer_layout)
            .setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        return root;
    }

    private fun setSwipeRefresh() {
        fragBinding.swipeRefresh.setOnRefreshListener {
            showLoader(loader)
            publicPlaylistsViewModel.getPublicPlaylistsFromDB()
            fragBinding.swipeRefresh.isRefreshing = true
        }
    }

    private fun checkSwipeRefresh() {
        if (fragBinding.swipeRefresh.isRefreshing)
            fragBinding.swipeRefresh.isRefreshing = false
    }

    private fun render(publicPlaylist: ArrayList<PublicPlaylistModel>) {
        fragBinding.recyclerViewForPlaylists.adapter = PublicPlaylistAdapter(publicPlaylist, this)
    }

    override fun onResume() {
        super.onResume()
        publicPlaylistsViewModel.load()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onPublicPlaylistClick(publicPlaylist: PublicPlaylistModel) {
        val action =
            PublicPlaylistsFragmentDirections.actionPublicPlaylistsFragmentToPublicPlaylistViewFragment(
                publicPlaylist.playlist.publicID
            )
        findNavController().navigate(action)
    }


}