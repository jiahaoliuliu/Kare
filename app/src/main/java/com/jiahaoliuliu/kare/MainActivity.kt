package com.jiahaoliuliu.kare

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.jiahaoliuliu.kare.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), GoogleMap.OnMarkerClickListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)
        val locationFragment = LocationFragment()
        locationFragment.setOnMarkerClickListener(this)
        supportFragmentManager.beginTransaction().add(R.id.container, locationFragment).commit()
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        val damagesFragment = DamagesFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, damagesFragment)
            .addToBackStack("DamagesFragment")
            .commit()
        return true
    }
}
