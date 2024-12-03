package com.example.musicplayer

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.databinding.ActivityPlaylistBinding

class PlaylistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlaylistBinding
    private val playlists = mutableListOf<Playlist>() // List of playlists

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*Ignore doesnt work yet
        val selectedSong: Song? = intent.getParcelableExtra("test")

        selectedSong?.let{
            val name = "Favorites"
            val playlist = playlists.find{ it.name == name }
            playlist?.songs?.add(it) ?: playlists.add(Playlist(name, mutableListOf(it)))
        }

         */

        // Temporary: Load some dummy playlists
        playlists.add(Playlist("Favorites", mutableListOf()))
        playlists.add(Playlist("Chill", mutableListOf()))

        binding.playlistRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.playlistRecyclerView.adapter = PlaylistAdapter(playlists) { playlist ->
            Toast.makeText(this, "Selected playlist: ${playlist.name}", Toast.LENGTH_SHORT).show()
        }
    }
}
