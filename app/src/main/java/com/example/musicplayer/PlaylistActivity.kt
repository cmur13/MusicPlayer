package com.example.musicplayer

import PlaylistAdapter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.databinding.ActivityPlaylistBinding

class PlaylistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlaylistBinding

    // List of playlists
    private val playlists = mutableListOf<Playlist>()

    // Temporary: Define your playlists
    private lateinit var favoritesPlaylist: Playlist
    private lateinit var chillPlaylist: Playlist

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Temporary: Load some dummy playlists
        favoritesPlaylist = Playlist("Favorites", mutableListOf())
        chillPlaylist = Playlist("Chill", mutableListOf())

        playlists.add(favoritesPlaylist)
        playlists.add(chillPlaylist)

        binding.playlistRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.playlistRecyclerView.adapter = PlaylistAdapter(playlists) { playlist ->
            Toast.makeText(this, "Selected playlist: ${playlist.name}", Toast.LENGTH_SHORT).show()
        }

    }

    // Show Add to Playlist or Delete Dialog
    fun showAddToPlaylistDialog(song: Song) {
        val options = arrayOf("Add to Playlist", "Delete")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Song Options")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> {
                    // Add to Playlist
                    showPlaylistSelectionDialog(song)
                }
                1 -> {
                    // Delete the song from the playlist
                    deleteSongFromPlaylist(song)
                }
            }
        }
        builder.show()
    }

    // Show Playlist Selection Dialog
    // Show Playlist Selection Dialog
    fun showPlaylistSelectionDialog(song: Song) {
        val playlists = arrayOf("Favorites", "Chill")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Playlist")
        builder.setItems(playlists) { _, which ->
            when (which) {
                0 -> {
                    // Add to Favorites
                    addSongToPlaylist(song, "Favorites")
                }
                1 -> {
                    // Add to Chill
                    addSongToPlaylist(song, "Chill")
                }
            }
        }
        builder.show()
    }


    fun showPlaylistSongs(playlistName: String) {
        val playlist = getPlaylistByName(playlistName)
        playlist?.let {
            // Update your RecyclerView to display only songs from this playlist
            (binding.playlistRecyclerView.adapter as PlaylistAdapter).updatePlaylist(it.songs, it.name)
        }
    }

    // Add song to playlist
    // Add song to playlist
    fun addSongToPlaylist(song: Song, playlistName: String) {
        val playlist = getPlaylistByName(playlistName)
        if (playlist != null) {
            playlist.songs.add(song)
            Log.d("MusicPlayer", "Added ${song.title} to $playlistName")
            updatePlaylistUI(playlistName)
        } else {
            Log.d("MusicPlayer", "Playlist not found: $playlistName")
        }
    }



    // Get playlist by name
    fun getPlaylistByName(name: String): Playlist? {
        return when (name) {
            "Favorites" -> favoritesPlaylist
            "Chill" -> chillPlaylist
            else -> null
        }
    }

    // Update the UI for the selected playlist
    // Update the UI for the selected playlist
    fun updatePlaylistUI(playlistName: String) {
        when (playlistName) {
            "Favorites" -> updatePlaylistRecyclerView(favoritesPlaylist)
            "Chill" -> updatePlaylistRecyclerView(chillPlaylist)
        }
    }

    // Update RecyclerView to show the updated playlist
    fun updatePlaylistRecyclerView(playlist: Playlist) {
        // Get the playlist's updated songs
        val updatedSongs = playlist.songs

        // Update the playlist in the adapter and notify for UI update
        (binding.playlistRecyclerView.adapter as PlaylistAdapter)
            .updatePlaylist(updatedSongs, playlist.name)
    }




    // Delete song from playlist (optional, not fully implemented)
    fun deleteSongFromPlaylist(song: Song) {
        Toast.makeText(this, "Song Deleted", Toast.LENGTH_SHORT).show()
    }
}


