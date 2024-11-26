package com.example.musicplayer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class SongAdapter(
    private val songs: List<Song>,
    private val onSongClick: (Song) -> Unit // Callback function for click events
) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.songTitle)
        val artistTextView: TextView = itemView.findViewById(R.id.songArtist)
        val durationTextView: TextView = itemView.findViewById(R.id.songDuration)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.song_item, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        holder.titleTextView.text = song.title
        holder.artistTextView.text = song.artist
        holder.durationTextView.text = song.duration

        holder.itemView.setOnClickListener {
            onSongClick(song)
        }

        holder.itemView.setOnLongClickListener {
            song.isFavorite = !song.isFavorite // Toggle favorite status
            Toast.makeText(
                holder.itemView.context,
                if (song.isFavorite) "Marked as favorite" else "Removed from favorites",
                Toast.LENGTH_SHORT
            ).show()
            true
        }
    }


    override fun getItemCount(): Int {
        return songs.size
    }
}

