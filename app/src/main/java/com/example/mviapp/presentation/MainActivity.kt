package com.example.mviapp.presentation

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.mviapp.R
import com.example.mviapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        attachListFragmentIfNeeded()
        setUpToolbar()
    }

    private fun attachListFragmentIfNeeded() {
        DocumentsListFragment.attachIfNeeded(
            containerId = R.id.documentContainer,
            fragmentManager = supportFragmentManager
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
    }
}