package com.nibm.myevents

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {


    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        if (savedInstanceState == null) {
            loadFragment(HomeFragment(), R.color.blue)
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> loadFragment(HomeFragment(), R.color.blue)
                R.id.nav_booking -> loadFragment(MyBookingsFragment(), R.color.yellow)
                R.id.nav_verifytickets -> loadFragment(VerifyTicketsFragment(), R.color.yellow)
                R.id.nav_profile -> loadFragment(ProfileFragment(), R.color.yellow)
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment, colorResId: Int) {
        val fragmentContainer = findViewById<androidx.fragment.app.FragmentContainerView>(R.id.home_fragment_container)
        fragmentContainer.setBackgroundColor(ContextCompat.getColor(this, colorResId))

        supportFragmentManager.beginTransaction()
            .replace(R.id.home_fragment_container, fragment)
            .commit()
    }
}

