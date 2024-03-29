package org.wit.playlistcreater.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.wit.playlistcreater.databinding.CardSongBinding
import org.wit.playlistcreater.models.songModel.Songs

interface SongClickListener {
    fun onSongClick(songs: Songs?)
}

class SongAdapter(private var songs: ArrayList<Songs?>, private val listener: SongClickListener) :
    RecyclerView.Adapter<SongAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardSongBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val song = songs[holder.adapterPosition]
        holder.bind(song!!)
    }

    fun removeAt(position: Int) {
        songs.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int = songs.size

    inner class MainHolder(val binding: CardSongBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(songs: Songs) {
            binding.root.tag = songs.track!!.id
            binding.song = songs
            Picasso.get().load(songs.track.album!!.images!![0].url).into(binding.songThumbnail)
            binding.root.setOnClickListener { listener.onSongClick(songs) }
            binding.executePendingBindings()
        }
    }
}