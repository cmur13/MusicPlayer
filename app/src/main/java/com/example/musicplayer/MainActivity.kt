package com.example.musicplayer

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.databinding.ActivityMainBinding
import java.io.File
import android.Manifest
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var musicPlayer: MusicPlayer // MusicPlayer instance
    private val songs = mutableListOf<Song>()
    private var favoriteView = false

    // new code
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var musicAdapter: SongAdapter

    companion object {
        private const val REQUEST_CODE = 1
        lateinit var MusicListMA: ArrayList<Song>
        lateinit var musicListSearch: ArrayList<Song>
        var search: Boolean = false
        var themeIndex: Int = 0
        var sortOrder: Int = 0
        val sortingList = arrayOf(
            MediaStore.Audio.Media.DATE_ADDED + " DESC",
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.SIZE + " DESC"
        )
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

        // new code
        val themeEditor = getSharedPreferences("THEMES", MODE_PRIVATE)
        themeIndex = themeEditor.getInt("themeIndex", 0)

        requestRuntimePermission()

        binding.MusicRv.setHasFixedSize(true)
        binding.MusicRv.layoutManager = LinearLayoutManager(this@MainActivity)

        val sortEditor = getSharedPreferences("SORTING", MODE_PRIVATE)
        sortOrder = sortEditor.getInt("sortOrder", 0)

        MusicListMA = getAllAudio()

        // Adapter
        musicAdapter = SongAdapter(this@MainActivity, MusicListMA){song->
            musicPlayer.playSong(song)
        }
        binding.MusicRv.adapter = musicAdapter

        binding.shuffleE.setOnClickListener {
            if (MusicListMA.isNotEmpty()) {
                val randomIndex = (0 until MusicListMA.size).random() // Generate a random index
                val intent = Intent(this, PlayerActivity::class.java)
                intent.putExtra("index", randomIndex) // Pass the random index
                intent.putExtra("class", "MainActivity")
                startActivity(intent)
            } else {
                Toast.makeText(this, "No songs available to shuffle!", Toast.LENGTH_SHORT).show()
            }
        }


        // For navigation drawer
        toggle = ActionBarDrawerToggle(this, binding.root, R.string.open, R.string.close)
        binding.root.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
    private fun loadSongsFromDevice() {
        songs.clear()
        songs.addAll(getAllAudio())

        musicPlayer = MusicPlayer(this, songs)

        // Update RecyclerView with all songs
        updateRecyclerView(songs)
    }

    private fun updateRecyclerView(songList: List<Song>) {
        val songAdapter = SongAdapter(this, ArrayList(songList)) { song -> // Use 'this' for Context and convert List<Song> to ArrayList
            musicPlayer.playSong(song)
        }

        binding.MusicRv.layoutManager = LinearLayoutManager(this)
        binding.MusicRv.adapter = songAdapter
    }

    /*
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

     */

    private fun formatDuration(duration: Long): String {
        val minutes = (duration / 1000) / 60
        val seconds = (duration / 1000) % 60
        return String.format("%d:%02d", minutes, seconds)
    }



    // new code
    // For runtime permission request
    private fun requestRuntimePermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 15
            )
        }
    }

    // Function to get all audio files from storage
    @SuppressLint("Range")
    private fun getAllAudio(): ArrayList<Song> {
        val tempList = ArrayList<Song>()
        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM_ID
        )
        val cursor = this.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null,
            sortingList[sortOrder], null
        )
        if (cursor != null) {
            if (cursor.moveToFirst())
                do {
                    val titleC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)) ?: "Unknown"
                    val idC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)) ?: "Unknown"
                    val albumC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)) ?: "Unknown"
                    val artistC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)) ?: "Unknown"
                    val pathC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val durationC = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                    val albumIdC = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)).toString()
                    val uri = Uri.parse("content://media/external/audio/albumart")
                    val artUriC = Uri.withAppendedPath(uri, albumIdC).toString()
                    val music = Song(
                        id = idC,
                        title = titleC,
                        album = albumC,
                        artist = artistC,
                        path = pathC,
                        duration = durationC,
                        artUri = artUriC
                    )
                    val file = File(music.path)
                    if (file.exists())
                        tempList.add(music)
                } while (cursor.moveToNext())
            cursor.close()
        }
        return tempList
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 15) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show()
                MusicListMA = getAllAudio()

                // Adapter
                musicAdapter = SongAdapter(this@MainActivity, MusicListMA){song->
                    musicPlayer.playSong(song)
                }
                binding.MusicRv.adapter = musicAdapter
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 15
                )
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::musicPlayer.isInitialized) {
            musicPlayer.release() // Release resources
        }
        if (!PlayerActivity.isPlaying && PlayerActivity.musicService != null) {
            PlayerActivity.musicService!!.stopForeground(true)
            PlayerActivity.musicService!!.mediaPlayer!!.release()
            PlayerActivity.musicService = null
            exitProcess(1)
        }
    }

    override fun onResume() {
        super.onResume()
        val sortEditor = getSharedPreferences("SORTING", MODE_PRIVATE)
        val sortValue = sortEditor.getInt("sortOrder", 0)
        if (sortOrder != sortValue) {
            sortOrder = sortValue
            MusicListMA = getAllAudio()
            musicAdapter.updateMusicList(MusicListMA)
        }
    }
}











