package com.fahrijalsyawaludin.aplikasistoryapp.detailmain

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.fahrijalsyawaludin.aplikasistoryapp.api.ListStoryItem
import com.fahrijalsyawaludin.aplikasistoryapp.databinding.ActivityDetailStoryBinding

class DetailMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val story = intent.getParcelableExtra<ListStoryItem>("story")

        Glide.with(this)
            .load(story?.photoUrl)
            .centerCrop()
            .into(binding.previewImageView)

        binding.tvName.text = story?.name
        binding.descriptionEditText.setText(story?.description)
    }
}