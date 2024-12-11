package com.example.musicplayer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var musicPlayer: MusicPlayer
    private val songs = mutableListOf<Song>()

    companion object {
        private const val REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
<<<<<<< Updated upstream

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
=======

        // Playlist click listener for "Favorites" playlist
        binding.playlistE.setOnClickListener {
            val selectedPlaylist = playlists.firstOrNull { it.name == "Favorites" }
            selectedPlaylist?.let {
                updateRecyclerView(it.songs)
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
>>>>>>> Stashed changes
            loadSongsFromDevice()
        } else {
            requestStoragePermission()
        }

        binding.shuffleE.setOnClickListener {
            if (::musicPlayer.isInitialized) {
                musicPlayer.shuffleAndPlay() // Call shuffle and play from MusicPlayer
            } else {
                Toast.makeText(this, "No songs available", Toast.LENGTH_SHORT).show()
            }
        }
<<<<<<< Updated upstream
    }

    private fun requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            Toast.makeText(this, "Storage permission is required to load songs", Toast.LENGTH_SHORT)
                .show()
        }
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadSongsFromDevice()
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadSongsFromDevice() {
        songs.clear()
        songs.addAll(getAllAudioFromDevice())

        musicPlayer = MusicPlayer(this, songs) // Initialize MusicPlayer with the song list

        val songAdapter = SongAdapter(songs) { song ->
            musicPlayer.playSong(song) // Play the selected song when clicked
        }
=======

        // Favorite button listener
        binding.favoriteE.setOnClickListener {
            val favoriteSongs = songs.filter { it.isFavorite }
            if (favoriteView) {
                favoriteView = false
                Toast.makeText(this, "Returning", Toast.LENGTH_SHORT).show()
                updateRecyclerView(songs)
            } else {
                if (favoriteSongs.isEmpty()) {
                    Toast.makeText(this, "No favorite songs available", Toast.LENGTH_SHORT).show()
                } else {
                    favoriteView = true
                    Toast.makeText(this, "Showing Favorites", Toast.LENGTH_SHORT).show()
                    updateRecyclerView(favoriteSongs)
                }
            }
        }

        // Previous song listener
        binding.prev.setOnClickListener {
            if (::musicPlayer.isInitialized) {
                musicPlayer.playPrevious()
            }
        }

        // Pause/Start song listener
        binding.pauseStart.setOnClickListener {
            if (::musicPlayer.isInitialized) {
                musicPlayer.pauseStart()
            }
        }

        // Next song listener
        binding.next.setOnClickListener {
            if (::musicPlayer.isInitialized) {
                musicPlayer.playNext()
            }
        }
    }

    private val playlists = mutableListOf(
        Playlist("Favorites", mutableListOf()),
        Playlist("Chill", mutableListOf())
    )

    private fun updateRecyclerView(songList: List<Song>) {
        val songAdapter = SongAdapter(songList, { song ->
            musicPlayer.playSong(song)
        }, playlists)
>>>>>>> Stashed changes

        binding.songsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.songsRecyclerView.adapter = songAdapter
    }

    private fun loadSongsFromDevice() {
        songs.clear()
        songs.addAll(getAllAudioFromDevice())
        musicPlayer = MusicPlayer(this, songs, binding)

        // Update RecyclerView with all songs
        updateRecyclerView(songs)
    }

    private fun getAllAudioFromDevice(): List<Song> {
        val songList = mutableListOf<Song>()
<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA
        )

<<<<<<< Updated upstream
        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
=======
        val selection = "${MediaStore.Audio.Media.DATA} LIKE ?"
        val selectionArgs = arrayOf("%/Download/%")
>>>>>>> Stashed changes
        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

        val cursor = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            sortOrder
        )

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                val artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                val duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
                val path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))

<<<<<<< Updated upstream
                val durationFormatted = formatDuration(duration)
                songList.add(Song(title, artist, durationFormatted, path))
=======
                    if (path.endsWith(".mp3", ignoreCase = true)) {
                        val durationFormatted = formatDuration(duration)
                        songList.add(Song(title, artist, durationFormatted, path))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
>>>>>>> Stashed changes
            }
            cursor.close()
        }
        return songList
    }

    private fun formatDuration(duration: Long): String {
        val minutes = (duration / 1000) / 60
        val seconds = (duration / 1000) % 60
        return String.format("%d:%02d", minutes, seconds)
    }

    private fun requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "Storage permission is required to load songs", Toast.LENGTH_SHORT).show()
        }
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadSongsFromDevice()
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::musicPlayer.isInitialized) {
            musicPlayer.release() // Release resources
        }
    }
}

<<<<<<< Updated upstream
=======


>>>>>>> Stashed changes
