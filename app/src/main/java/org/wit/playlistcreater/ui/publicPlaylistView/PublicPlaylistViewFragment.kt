package org.wit.playlistcreater.ui.publicPlaylistView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import org.wit.playlistcreater.adapters.SongAdapter
import org.wit.playlistcreater.adapters.SongClickListener
import org.wit.playlistcreater.databinding.FragmentPublicPlaylistViewBinding
import org.wit.playlistcreater.models.songModel.Songs
import org.wit.playlistcreater.utils.customTransformation

class PublicPlaylistViewFragment : Fragment(), SongClickListener {

    private val args by navArgs<PublicPlaylistViewFragmentArgs>()
    private var _fragBinding: FragmentPublicPlaylistViewBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var publicPlaylistViewViewModel: PublicPlaylistViewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentPublicPlaylistViewBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        fragBinding.recyclerViewForSongsInPlaylist.layoutManager = LinearLayoutManager(activity)
        publicPlaylistViewViewModel =
            ViewModelProvider(this)[PublicPlaylistViewViewModel::class.java]
        publicPlaylistViewViewModel.getSongsInPlaylist(args.publicPlaylistId)
        publicPlaylistViewViewModel.observablePlaylistSongs.observe(
            viewLifecycleOwner,
            Observer { song ->
                render(song as ArrayList<Songs?>)
            })

        setLikeBtnListener(fragBinding)
        setUnlikeBtn(fragBinding)
        return root
    }

    private fun setLikeBtnListener(fragBinding: FragmentPublicPlaylistViewBinding) {
        fragBinding.like.setOnClickListener {
            publicPlaylistViewViewModel.updateLikeCount(args.publicPlaylistId)
            fragBinding.like.visibility = View.GONE
        }
    }

    private fun setUnlikeBtn(fragBinding: FragmentPublicPlaylistViewBinding) {
        fragBinding.unlike.setOnClickListener {
            publicPlaylistViewViewModel.unlikePlaylist(args.publicPlaylistId)
        }
    }


    private fun render(songs: ArrayList<Songs?>) {
        fragBinding.recyclerViewForSongsInPlaylist.adapter = SongAdapter(songs, this)
        fragBinding.playlistTitle.text =
            publicPlaylistViewViewModel.getPublicPlaylist(args.publicPlaylistId)!!.playlist!!.title
        fragBinding.genre.text =
            publicPlaylistViewViewModel.getPublicPlaylist(args.publicPlaylistId)!!.playlist!!.playListGenre
        fragBinding.count.text =
            publicPlaylistViewViewModel.getPublicPlaylist(args.publicPlaylistId)!!.playlist!!.songs!!.size.toString()

        Picasso.get()
            .load(publicPlaylistViewViewModel.getPublicPlaylist(args.publicPlaylistId)!!.profilePic!!.toUri())
            .resize(200, 200)
            .transform(customTransformation())
            .centerCrop()
            .into(fragBinding.publicPlaylistProfilePic)

        fragBinding.userName.text =
            publicPlaylistViewViewModel.getPublicPlaylist(args.publicPlaylistId)!!.displayName


        if (publicPlaylistViewViewModel.getPublicPlaylist(args.publicPlaylistId)!!.uid == FirebaseAuth.getInstance().currentUser!!.uid) {
            fragBinding.like.visibility = View.GONE
        }

        if (publicPlaylistViewViewModel.isPlaylistLiked(args.publicPlaylistId)) {
            fragBinding.like.visibility = View.GONE
        }
    }

    override fun onSongClick(songs: Songs?) {
        val action =
            PublicPlaylistViewFragmentDirections.actionPublicPlaylistViewFragmentToSongInfoFragment(
                songs!!.track!!.id.toString()
            ).setCameFromPlaylist(true)
        findNavController().navigate(action)
    }

}