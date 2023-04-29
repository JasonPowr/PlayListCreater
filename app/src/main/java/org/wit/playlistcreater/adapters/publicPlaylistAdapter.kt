package org.wit.playlistcreater.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.wit.playlistcreater.databinding.CardPublicPlaylistBinding
import org.wit.playlistcreater.models.publicPlaylistModel.PublicPlaylistModel
import org.wit.playlistcreater.utils.customTransformation

interface PublicPlaylistClickListener {
    fun onPublicPlaylistClick(publicPlaylist: PublicPlaylistModel)
}

class PublicPlaylistAdapter(
    private var publicPlaylists: ArrayList<PublicPlaylistModel>,
    private val listner: PublicPlaylistClickListener
) : RecyclerView.Adapter<PublicPlaylistAdapter.MainHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PublicPlaylistAdapter.MainHolder {
        val binding = CardPublicPlaylistBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: PublicPlaylistAdapter.MainHolder, position: Int) {
        val publicPlaylists = publicPlaylists[holder.adapterPosition]
        holder.bind(publicPlaylists)
    }

    override fun getItemCount(): Int = publicPlaylists.size

    inner class MainHolder(val binding: CardPublicPlaylistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(publicPlaylist: PublicPlaylistModel) {
            binding.root.tag = publicPlaylist.playlist!!.id
            binding.publicPlaylist = publicPlaylist
            binding.root.setOnClickListener { listner.onPublicPlaylistClick(publicPlaylist) }

            Picasso.get().load(publicPlaylist.profilePic!!.toUri())
                .resize(200, 200)
                .transform(customTransformation())
                .centerCrop()
                .into(binding.profileImage)

            binding.executePendingBindings()
        }
    }
}

