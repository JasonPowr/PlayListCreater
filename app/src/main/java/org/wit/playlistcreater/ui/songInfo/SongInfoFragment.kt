package org.wit.playlistcreater.ui.songInfo

import android.media.AudioManager
import android.media.MediaPlayer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.playlistcreater.R
import org.wit.playlistcreater.adapters.PlaylistAdapter
import org.wit.playlistcreater.databinding.FragmentCreatePlaylistBinding
import org.wit.playlistcreater.databinding.FragmentSongInfoBinding
import org.wit.playlistcreater.models.AppManager
import org.wit.playlistcreater.models.playlistModel.PlaylistModel
import org.wit.playlistcreater.models.songModel.Song
import org.wit.playlistcreater.ui.createPlaylist.CreatePlaylistViewModel
import org.wit.playlistcreater.ui.playlistList.PlaylistViewModel

class SongInfoFragment : Fragment() {
    private val args by navArgs<SongInfoFragmentArgs>()
    private var _fragBinding: FragmentSongInfoBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var songInfoViewModel: SongInfoViewModel
    lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _fragBinding = FragmentSongInfoBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        songInfoViewModel = ViewModelProvider(this)[SongInfoViewModel::class.java]
        songInfoViewModel.getSong(args.songId)
        songInfoViewModel.observableSong.observe(viewLifecycleOwner, Observer {
                song ->
            song?.let { render(song) }
        })
        setMediaPlayerListner(fragBinding)
        return root;
    }

    private fun setMediaPlayerListner(layout: FragmentSongInfoBinding){
        mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(songInfoViewModel.observableSong.value!!.track.preview_url)
        mediaPlayer.prepare()

        layout.playBtn.setOnClickListener{
                mediaPlayer.start()
        }

        layout.pauseBtn.setOnClickListener{
                mediaPlayer.pause()
        }

        //https://www.geeksforgeeks.org/play-audio-from-url-in-android-using-kotlin/
    }


    private fun render(song: Song) {
        fragBinding.songName.text = song.track.name
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

}