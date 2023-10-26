package com.fahrijalsyawaludin.aplikasistoryapp.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.fahrijalsyawaludin.aplikasistoryapp.adapter.ListStoriesAdapter
import com.fahrijalsyawaludin.aplikasistoryapp.addstory.AddStoryActivity
import com.fahrijalsyawaludin.aplikasistoryapp.api.ListStoryItem
import com.fahrijalsyawaludin.aplikasistoryapp.databinding.ActivityMainBinding
import com.fahrijalsyawaludin.aplikasistoryapp.view.ViewModelFactory
import com.fahrijalsyawaludin.aplikasistoryapp.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!viewModel.getSession().isLogin) {
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        }
        setupAction()

        viewModel.infoLoading.observe(this) {
            binding.loadingProgressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.infoResponse.observe(this) {
            if (!it.error!!) {
                val list = it.listStory as ArrayList<ListStoryItem>
                showRecyclerList(list)
            }
        }
        binding.fab.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }

    }

    private fun showRecyclerList(list: ArrayList<ListStoryItem>) {
        binding.rvSd.layoutManager = LinearLayoutManager(this)

        val listStoriesAdapter = ListStoriesAdapter(list)
        binding.rvSd.adapter = listStoriesAdapter
    }

    private fun setupAction() {

    }

}