package com.example.musicplayer

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var musicPlayer: MusicPlayer // MusicPlayer instance
    private val songs = mutableListOf<Song>()
    private var favoriteView = false

    companion object {
        private const val REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.playlistE.setOnClickListener {
            val intent = Intent(this, PlaylistActivity::class.java)
            startActivity(intent)
        }


        // Check for storage permissions
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            loadSongsFromDevice()
        } else {
            requestStoragePermission()
        }

        // Shuffle button listener
        binding.shuffleE.setOnClickListener {
            if (::musicPlayer.isInitialized) {
                musicPlayer.shuffleAndPlay()
            } else {
                Toast.makeText(this, "No songs available", Toast.LENGTH_SHORT).show()
            }
        }

        // Favorites button listener
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
        //Previous song imageview listener
        binding.prev.setOnClickListener{
            if (::musicPlayer.isInitialized) {
                musicPlayer.playPrevious()
            }
        }

        // Pause Song/ Resume Song listener
        binding.pauseStart.setOnClickListener{
            if(::musicPlayer.isInitialized) {
                musicPlayer.pauseStart()
            }
        }

        //Next song imageview listener
        binding.next.setOnClickListener{
            if(::musicPlayer.isInitialized) {
                musicPlayer.playNext()
            }
        }

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

        musicPlayer = MusicPlayer(this, songs)

        // Update RecyclerView with all songs
        updateRecyclerView(songs)
    }

    private fun updateRecyclerView(songList: List<Song>) {
        val songAdapter = SongAdapter(songList) { song ->
            musicPlayer.playSong(song)
        }

        binding.songsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.songsRecyclerView.adapter = songAdapter
    }

    private fun getAllAudioFromDevice(): List<Song> {
        val songList = mutableListOf<Song>()

        // Define the projection to fetch song details
        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA
        )

        // Define selection to filter MP3 files specifically in the Download directory
        val selection = "${MediaStore.Audio.Media.DATA} LIKE ?"
        val selectionArgs = arrayOf("%/Download/%") // Focus on files in the Download directory

        // Sort the results alphabetically by title
        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

        val cursor = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )

        if (cursor != null) {
            while (cursor.moveToNext()) {
                try {
                    val title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                    val artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                    val duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
                    val path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))

                    // Only add MP3 files (double-check extension)
                    if (path.endsWith(".mp3", ignoreCase = true)) {
                        val durationFormatted = formatDuration(duration)
                        songList.add(Song(title, artist, durationFormatted, path))
                    }
                } catch (e: Exception) {
                    e.printStackTrace() // Log any issues for debugging
                }
            }
            cursor.close()
        } else {
            // Log if the cursor is null (no results from query)
            println("Cursor is null! No songs found.")
        }

        return songList
    }



    private fun formatDuration(duration: Long): String {
        val minutes = (duration / 1000) / 60
        val seconds = (duration / 1000) % 60
        return String.format("%d:%02d", minutes, seconds)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::musicPlayer.isInitialized) {
            musicPlayer.release() // Release resources
        }
    }
}


