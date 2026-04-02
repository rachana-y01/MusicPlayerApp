package com.example.musicapp

import android.annotation.SuppressLint
import android.media.MediaPlayer
import com.example.musicapp.R
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var btnPlayPause: ImageButton
    private lateinit var btnNext: ImageButton
    private lateinit var btnPrev: ImageButton
    private lateinit var seekBar: SeekBar
    private lateinit var songTitle: TextView
    private lateinit var backgroundImage: ImageView
    private lateinit var currentTime: TextView
    private lateinit var totalDuration: TextView
    private lateinit var favoriteIcon: ImageView
    private lateinit var btnBack: ImageButton

    private val songs = listOf(
        R.raw.song1, R.raw.song2, R.raw.song3, R.raw.song4, R.raw.song5, R.raw.song6,R.raw.song7,R.raw.song8,R.raw.song9,R.raw.song10
    )

    private val songTitles = listOf(
        "Hymn for the Weekend","Believer","Thunder","Paradise","Mi Gente","Ve Maahi","O Re Piya","Diet Mountain Dew","Espresso","Under The Influence "
    )

    private val backgroundImages = listOf(
        R.drawable.bg1, R.drawable.bg2, R.drawable.bg3, R.drawable.bg4, R.drawable.bg5,R.drawable.bg6,R.drawable.bg7,R.drawable.bg8,R.drawable.bg9,R.drawable.bg10
    )

    private var currentIndex = 0
    private val handler = Handler(Looper.getMainLooper())
    private var isPlaying = false
    private val favoriteSongsManager by lazy { FavoriteSongsManager() }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout)

        currentIndex = intent.getIntExtra("SONG_INDEX", 0)
        initializeViews()
        initializeMediaPlayer()
    }

    private fun initializeViews() {
        btnPlayPause = findViewById(R.id.btnPlayPause)
        btnNext = findViewById(R.id.btnNext)
        btnPrev = findViewById(R.id.btnPrev)
        seekBar = findViewById(R.id.seekBar)
        songTitle = findViewById(R.id.songTitle)
        backgroundImage = findViewById(R.id.albumCover)
        currentTime = findViewById(R.id.currentTime)
        totalDuration = findViewById(R.id.totalDuration)
        favoriteIcon = findViewById(R.id.favoriteIcon)
        btnBack = findViewById(R.id.btnBack)

        btnPlayPause.setOnClickListener { togglePlayPause() }
        btnNext.setOnClickListener { playNextSong() }
        btnPrev.setOnClickListener { playPreviousSong() }
        favoriteIcon.setOnClickListener { toggleFavorite() }
        btnBack.setOnClickListener { finish() }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) mediaPlayer?.seekTo(progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun togglePlayPause() {
        mediaPlayer?.let {
            if (isPlaying) {
                it.pause()
                btnPlayPause.setImageResource(android.R.drawable.ic_media_play)
            } else {
                it.start()
                btnPlayPause.setImageResource(android.R.drawable.ic_media_pause)
                updateSeekBar()
            }
            isPlaying = !isPlaying
        }
    }

    private fun toggleFavorite() {
        if (favoriteSongsManager.isFavorite(currentIndex)) {
            favoriteSongsManager.removeFavorite(currentIndex)
            favoriteIcon.setImageResource(R.drawable.ic_heart_outline)
            Toast.makeText(this, "${songTitles[currentIndex]} removed from favorites", Toast.LENGTH_SHORT).show()
        } else {
            favoriteSongsManager.addFavorite(currentIndex)
            favoriteIcon.setImageResource(R.drawable.ic_heart_filled)
            Toast.makeText(this, "${songTitles[currentIndex]} added to favorites", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initializeMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(this, songs[currentIndex]).apply {
            setOnCompletionListener { playNextSong() }
        }
        updateUI()
    }

    private fun updateUI() {
        songTitle.text = songTitles[currentIndex]
        backgroundImage.setImageResource(backgroundImages[currentIndex])
        seekBar.max = mediaPlayer?.duration ?: 0
        totalDuration.text = formatDuration(mediaPlayer?.duration ?: 0)
        updateFavoriteIcon()
    }

    private fun updateFavoriteIcon() {
        favoriteIcon.setImageResource(
            if (favoriteSongsManager.isFavorite(currentIndex)) R.drawable.ic_heart_filled else R.drawable.ic_heart_outline
        )
    }

    private fun playNextSong() {
        currentIndex = (currentIndex + 1) % songs.size
        restartMediaPlayer()
    }

    private fun playPreviousSong() {
        currentIndex = if (currentIndex - 1 < 0) songs.size - 1 else currentIndex - 1
        restartMediaPlayer()
    }

    private fun restartMediaPlayer() {
        initializeMediaPlayer()
        mediaPlayer?.start()
        isPlaying = true
        btnPlayPause.setImageResource(android.R.drawable.ic_media_pause)
        updateSeekBar()
    }

    private fun updateSeekBar() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                mediaPlayer?.let {
                    seekBar.progress = it.currentPosition
                    currentTime.text = formatDuration(it.currentPosition)
                    if (it.isPlaying) handler.postDelayed(this, 500)
                }
            }
        }, 500)
    }

    private fun formatDuration(duration: Int): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration.toLong())
        val seconds = TimeUnit.MILLISECONDS.toSeconds(duration.toLong()) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer?.pause()
    }

    override fun onResume() {
        super.onResume()
        if (isPlaying) mediaPlayer?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}