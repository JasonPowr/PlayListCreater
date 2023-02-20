package org.wit.playlistcreater.ui.songInfo

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.wit.playlistcreater.databinding.FragmentSongInfoBinding
import org.wit.playlistcreater.models.songModel.Songs

class SongInfoFragment : Fragment() {

    private val args by navArgs<SongInfoFragmentArgs>()
    private var _fragBinding: FragmentSongInfoBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var songInfoViewModel: SongInfoViewModel
    lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentSongInfoBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        songInfoViewModel = ViewModelProvider(this)[SongInfoViewModel::class.java]
        songInfoViewModel.getSong(args.songId)
        songInfoViewModel.observableSongs.observe(viewLifecycleOwner, Observer { song ->
            song?.let { render(song) }
        })
        setMediaPlayerListner(fragBinding)
        setAddToPlaylistBtn(fragBinding)
        setDeleteFromPlaylistBtn(fragBinding)
        return root;
    }

    private fun setAddToPlaylistBtn(layout: FragmentSongInfoBinding) {
        if (args.cameFromPlaylist) {
            layout.addSongToPlaylistBtn.visibility = View.GONE
        } else {
            layout.addSongToPlaylistBtn.setOnClickListener {
                val action = SongInfoFragmentDirections.actionSongInfoFragmentToPlaylistFragment3()
                    .setSongId(args.songId)
                findNavController().navigate(action)
            }
        }
    }

    private fun setDeleteFromPlaylistBtn(layout: FragmentSongInfoBinding) {
        if (!songInfoViewModel.observableSongs.value!!.isInPlaylist || !args.cameFromPlaylist) {
            layout.deleteSongFromPlaylistBtn.visibility = View.GONE
        } else {
            layout.deleteSongFromPlaylistBtn.setOnClickListener {
                songInfoViewModel.deleteSongFromPlaylist(
                    args.songId,
                    songInfoViewModel.getPlaylist(args.playlistId)!!
                )
                findNavController().popBackStack()
            }
        }
    }

    private fun setMediaPlayerListner(layout: FragmentSongInfoBinding) {
        var isStopped = false
        mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(songInfoViewModel.observableSongs.value!!.track.preview_url)
        mediaPlayer.prepare()

        layout.playBtn.setOnClickListener {
            if (isStopped) {
                mediaPlayer = MediaPlayer()
                mediaPlayer.setDataSource(songInfoViewModel.observableSongs.value!!.track.preview_url)
                mediaPlayer.prepare()
                mediaPlayer.start()
            } else {
                mediaPlayer.start()
            }
        }

        layout.pauseBtn.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            }
        }

        layout.stopBtn.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
                mediaPlayer.reset()
                mediaPlayer.release()
                isStopped = true
            }
        }
        //https://www.geeksforgeeks.org/play-audio-from-url-in-android-using-kotlin/
    }


    private fun render(songs: Songs) {
        fragBinding.songName.text = songs.track.name
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

}