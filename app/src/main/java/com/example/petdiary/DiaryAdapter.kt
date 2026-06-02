package com.example.petdiary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DiaryAdapter(
    private val entries: MutableList<DiaryEntry>
) : RecyclerView.Adapter<DiaryAdapter.EntryViewHolder>() {

    class EntryViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        val text: TextView =
            view.findViewById(R.id.entryText)

        val image: ImageView =
            view.findViewById(R.id.entryImage)

        val time: TextView =
            view.findViewById(R.id.entryTime)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EntryViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_diary_entry,
                parent,
                false
            )

        return EntryViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: EntryViewHolder,
        position: Int
    ) {

        val entry = entries[position]

        holder.text.text = entry.text

        holder.time.text = entry.time

        if (entry.imageUri != null) {

            holder.image.visibility = View.VISIBLE

            holder.image.setImageURI(entry.imageUri)

        } else {

            holder.image.visibility = View.GONE
        }
    }

    override fun getItemCount() = entries.size
}