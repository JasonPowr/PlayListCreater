package org.wit.playlistcreater.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.wit.playlistcreater.databinding.CardPlaylistBinding
import org.wit.playlistcreater.models.playlistModel.PlaylistModel

class PlaylistAdapter(private var playlists: List<PlaylistModel>)
    : RecyclerView.Adapter<PlaylistAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardPlaylistBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val playlist = playlists[holder.adapterPosition]
        holder.bind(playlist)
    }

    override fun getItemCount(): Int = playlists.size

    inner class MainHolder(val binding : CardPlaylistBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(playlist: PlaylistModel) {
            binding.playlist = playlist
            binding.executePendingBindings()
        }
    }
}