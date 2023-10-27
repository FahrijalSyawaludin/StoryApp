package com.fahrijalsyawaludin.aplikasistoryapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fahrijalsyawaludin.aplikasistoryapp.R
import com.fahrijalsyawaludin.aplikasistoryapp.api.ListStoryItem
import com.fahrijalsyawaludin.aplikasistoryapp.detailmain.DetailMainActivity

class ListStoriesAdapter(private val listStories: ArrayList<ListStoryItem>) :
    RecyclerView.Adapter<ListStoriesAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_sd, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listStories[position])
    }

    override fun getItemCount(): Int = listStories.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var imgPhoto: ImageView = itemView.findViewById(R.id.profileImageView)
        private var tvName: TextView = itemView.findViewById(R.id.nameTextView)
        private var tvDescription: TextView = itemView.findViewById(R.id.descTextView)

        fun bind(stories: ListStoryItem) {
            Glide.with(itemView.context)
                .load(stories.photoUrl)
                .centerCrop()
                .into(imgPhoto)
            tvName.text = stories.name
            tvDescription.text = stories.description

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailMainActivity::class.java)
                intent.putExtra("story", stories)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(imgPhoto, "profile"),
                        Pair(tvName, "name"),
                        Pair(tvDescription, "description"),
                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

}