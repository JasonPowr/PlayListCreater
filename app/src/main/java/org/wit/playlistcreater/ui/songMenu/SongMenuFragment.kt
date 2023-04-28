package org.wit.playlistcreater.ui.songMenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import org.wit.playlistcreater.databinding.FragmentSongMenuBinding

class SongMenuFragment : Fragment() {
    private var _fragBinding: FragmentSongMenuBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var songMenuViewModel: SongMenuViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentSongMenuBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        songMenuViewModel = ViewModelProvider(this)[SongMenuViewModel::class.java]

        setOption1Listener(fragBinding)
        setOption2Listener(fragBinding)
        setOption3Listener(fragBinding)
        return root;
    }

    private fun setOption1Listener(fragBinding: FragmentSongMenuBinding) {
        fragBinding.option1.setOnClickListener {
            val action = SongMenuFragmentDirections.actionSongMenuFragmentToSongFragment("option1")
            findNavController().navigate(action)
        }
    }

    private fun setOption2Listener(fragBinding: FragmentSongMenuBinding) {
        fragBinding.option2.setOnClickListener {
            val action = SongMenuFragmentDirections.actionSongMenuFragmentToSongFragment("option2")
            findNavController().navigate(action)
        }
    }


    private fun setOption3Listener(fragBinding: FragmentSongMenuBinding) {
        fragBinding.option3.setOnClickListener {
            val action = SongMenuFragmentDirections.actionSongMenuFragmentToSongFragment("option3")
            findNavController().navigate(action)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

}