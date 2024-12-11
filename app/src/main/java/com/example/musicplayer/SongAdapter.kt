package com.example.musicplayer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.PopupMenu


//This is song_item.xml
class SongAdapter(
    private val songs: List<Song>,
    private val onSongClick: (Song) -> Unit // Callback function for click events
) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.songTitle)
        val artistTextView: TextView = itemView.findViewById(R.id.songArtist)
        val durationTextView: TextView = itemView.findViewById(R.id.songDuration)
        val favoriteIcon: ImageView = itemView.findViewById(R.id.favoriteIcon) // ImageView for favorite icon
        val moreIcon: ImageView = itemView.findViewById(R.id.moreIcon) //ImageView for more options (add/delete/etc.)
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

        // Load the favorite state from SharedPreferences
        val sharedPreferences: SharedPreferences = holder.itemView.context.getSharedPreferences("Favorites", Context.MODE_PRIVATE)
        val isFavorite = sharedPreferences.getBoolean(song.title, false) // Default to false if not found
        song.isFavorite = isFavorite

        // Set the favorite icon based on the song's favorite status
        if (song.isFavorite) {
            holder.favoriteIcon.setImageResource(R.drawable.favourite_icon) // Filled star
        } else {
            holder.favoriteIcon.setImageResource(R.drawable.ic_favorite_border) // Empty star
        }

        holder.itemView.setOnClickListener {
            onSongClick(song)
        }

        holder.favoriteIcon.setOnClickListener {
            song.isFavorite = !song.isFavorite // Toggle favorite status
            // Save the updated favorite status
            saveFavoriteState(holder.itemView.context, song.title, song.isFavorite)

            // Update the icon and show toast
            if (song.isFavorite) {
                holder.favoriteIcon.setImageResource(R.drawable.favourite_icon) // Filled star
                Toast.makeText(holder.itemView.context, "Marked as favorite", Toast.LENGTH_SHORT).show()
            } else {
                holder.favoriteIcon.setImageResource(R.drawable.ic_favorite_border) // Empty star
                Toast.makeText(holder.itemView.context, "Removed from favorites", Toast.LENGTH_SHORT).show()
            }
        }

        holder.moreIcon.setOnClickListener{
            val popupMenu = PopupMenu(holder.itemView.context,holder.moreIcon)
            popupMenu.menuInflater.inflate(R.menu.popup, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener{ menuItem ->
                val selected = songs[position]
                val intent = Intent(holder.itemView.context, PlaylistActivity::class.java)
                //intent.putExtra("test", selected)
                holder.itemView.context.startActivity(intent)
                true
            }
            popupMenu.show()
        }
    }


    private fun saveFavoriteState(context: Context, songId: String, isFavorite: Boolean) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("Favorites", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(songId, isFavorite)
        editor.apply() // Apply changes to persist the state
    }

    override fun getItemCount(): Int {
        return songs.size
    }
}


