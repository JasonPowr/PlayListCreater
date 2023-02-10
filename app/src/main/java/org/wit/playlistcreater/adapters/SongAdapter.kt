package org.wit.playlistcreater.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.wit.playlistcreater.databinding.CardSongBinding
import org.wit.playlistcreater.models.songModel.Songs

interface SongClickListener {
    fun onSongClick(songs: Songs?)
}

class SongAdapter(private var songs: List<Songs?>, private val listener: SongClickListener)
    : RecyclerView.Adapter<SongAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardSongBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val song = songs[holder.adapterPosition]
        holder.bind(song!!)
    }

    override fun getItemCount(): Int = songs.size

    inner class MainHolder(val binding : CardSongBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(songs: Songs) {
            binding.song = songs
            binding.root.setOnClickListener{listener.onSongClick(songs)}
            binding.executePendingBindings()
        }
    }
}