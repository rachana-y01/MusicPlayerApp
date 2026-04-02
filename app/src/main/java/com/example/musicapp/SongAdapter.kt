package com.example.musicapp
import com.example.musicapp.R

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SongAdapter(
    private val songTitles: List<String>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_song, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(songTitles[position])
        holder.itemView.setOnClickListener { onItemClick(position) }
    }

    override fun getItemCount(): Int = songTitles.size

    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.songName)
        private val musicNoteIcon: ImageView = itemView.findViewById(R.id.musicNoteIcon)

        fun bind(title: String) {
            textView.text = title
            musicNoteIcon.setImageResource(R.drawable.ic_music_note) // Ensure the icon is displayed
        }
    }
}