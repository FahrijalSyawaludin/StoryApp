package com.fahrijalsyawaludin.aplikasistoryapp.main

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.fahrijalsyawaludin.aplikasistoryapp.R
import com.fahrijalsyawaludin.aplikasistoryapp.adapter.ListStoriesAdapter
import com.fahrijalsyawaludin.aplikasistoryapp.addstory.AddStoryActivity
import com.fahrijalsyawaludin.aplikasistoryapp.api.ListStoryItem
import com.fahrijalsyawaludin.aplikasistoryapp.databinding.ActivityMainBinding
import com.fahrijalsyawaludin.aplikasistoryapp.maps.MapsActivity
import com.fahrijalsyawaludin.aplikasistoryapp.view.ViewModelFactory
import com.fahrijalsyawaludin.aplikasistoryapp.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val isDarkModeEnabled = sharedPreferences.getBoolean("darkMode", false)

        if (isDarkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

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


    }

    private fun showRecyclerList(list: ArrayList<ListStoryItem>) {
        binding.rvSd.layoutManager = LinearLayoutManager(this)

        val listStoriesAdapter = ListStoriesAdapter(list)
        binding.rvSd.adapter = listStoriesAdapter
    }

    private fun setupAction() {
        binding.fab.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }

        binding.fabLogout.setOnClickListener {
            viewModel.logout()
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val isDarkModeEnabled = sharedPreferences.getBoolean("darkMode", false)
        menu?.findItem(R.id.menu_dark_mode)?.isChecked = isDarkModeEnabled

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_dark_mode -> {
                val isDarkModeEnabled = !item.isChecked
                item.isChecked = isDarkModeEnabled

                sharedPreferences.edit().putBoolean("darkMode", isDarkModeEnabled).apply()

                if (isDarkModeEnabled) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                return true
            }

            R.id.menu_refresh -> {
                viewModel.getAllStories(viewModel.getSession().token!!)
                return true
            }
            R.id.menu_maps -> {
                startActivity(Intent(this, MapsActivity::class.java))
                return true
            }

        }

        return super.onOptionsItemSelected(item)
    }

}