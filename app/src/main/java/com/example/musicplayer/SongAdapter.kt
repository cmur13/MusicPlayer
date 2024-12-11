package com.example.musicplayer

import PlaylistAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
<<<<<<< Updated upstream
=======
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
>>>>>>> Stashed changes

class SongAdapter(
    private val songs: List<Song>,
    private val onSongClick: (Song) -> Unit, // Callback function for click events
    private val playlists: MutableList<Playlist> // Accept playlists here
) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.songTitle)
        val artistTextView: TextView = itemView.findViewById(R.id.songArtist)
        val durationTextView: TextView = itemView.findViewById(R.id.songDuration)
<<<<<<< Updated upstream
=======
        val favoriteIcon: ImageView = itemView.findViewById(R.id.favoriteIcon) // ImageView for favorite icon
        val moreIcon: ImageView = itemView.findViewById(R.id.moreIcon) // ImageView for more options (add/delete/etc.)
>>>>>>> Stashed changes
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.song_item, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
<<<<<<< Updated upstream
        holder.titleTextView.text = song.title
        holder.artistTextView.text = song.artist
        holder.durationTextView.text = song.duration

        // Set click listener
        holder.itemView.setOnClickListener {
            onSongClick(song) // Pass the clicked song to the callback
        }
=======

        // Set the favorite icon based on the song's favorite status
        if (song.isFavorite) {
            holder.favoriteIcon.setImageResource(R.drawable.favourite_icon) // Filled star
        } else {
            holder.favoriteIcon.setImageResource(R.drawable.ic_favorite_border) // Empty star
        }

        // Handle click to toggle favorite status
        holder.favoriteIcon.setOnClickListener {
            song.isFavorite = !song.isFavorite // Toggle favorite status
            saveFavoriteState(holder.itemView.context, song.title, song.isFavorite)

            // Update the icon immediately
            if (song.isFavorite) {
                holder.favoriteIcon.setImageResource(R.drawable.favourite_icon) // Filled star
                Toast.makeText(holder.itemView.context, "Marked as favorite", Toast.LENGTH_SHORT).show()
            } else {
                holder.favoriteIcon.setImageResource(R.drawable.ic_favorite_border) // Empty star
                Toast.makeText(holder.itemView.context, "Removed from favorites", Toast.LENGTH_SHORT).show()
            }

            // Notify the adapter that the item changed so the UI updates
            notifyItemChanged(position)
        }



        // Handle more options menu click
        holder.moreIcon.setOnClickListener {
            val popupMenu = PopupMenu(holder.itemView.context, holder.moreIcon)
            popupMenu.menuInflater.inflate(R.menu.popup, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                val selectedSong = songs[position]

                when (menuItem.itemId) {
                    R.id.add_to_playlist -> {
                        showPlaylistSelectionDialog(holder.itemView.context, selectedSong)
                        return@setOnMenuItemClickListener true
                    }
                    R.id.delete_song -> {
                        // Handle song deletion
                        return@setOnMenuItemClickListener true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }
    }

    private fun showPlaylistSelectionDialog(context: Context, selectedSong: Song) {
        val playlists = listOf("Favorites", "Chill")  // List of your playlists

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Add to Playlist")
        builder.setItems(playlists.toTypedArray()) { _, which ->
            when (which) {
                0 -> {
                    // Add to Favorites playlist
                    addSongToPlaylist(selectedSong, "Favorites")
                    Toast.makeText(context, "Added to Favorites", Toast.LENGTH_SHORT).show()
                }
                1 -> {
                    // Add to Chill playlist
                    addSongToPlaylist(selectedSong, "Chill")
                    Toast.makeText(context, "Added to Chill", Toast.LENGTH_SHORT).show()
                }
            }
        }
        builder.show()
    }

    private fun addSongToPlaylist(song: Song, playlistName: String) {
        val playlist = playlists.find { it.name == playlistName }

        playlist?.let {
            it.songs.add(song)
            Log.d("Playlist", "Song added to $playlistName")
        }
    }

    private fun saveFavoriteState(context: Context, songId: String, isFavorite: Boolean) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("Favorites", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(songId, isFavorite)
        editor.apply()
>>>>>>> Stashed changes
    }

    override fun getItemCount(): Int {
        return songs.size
    }
}
<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
