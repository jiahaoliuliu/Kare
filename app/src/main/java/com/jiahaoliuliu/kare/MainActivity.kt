package com.jiahaoliuliu.kare

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.jiahaoliuliu.kare.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var googleMap: GoogleMap? = null
    private var locationPermissionGranted = false
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var lastKnowLocation: Location? = null

    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1000
        private const val DEFAULT_ZOOM = 20
        private const val TAG = "KARE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)
        val supportMapFragment = supportFragmentManager.findFragmentById(R.id.maps) as SupportMapFragment
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        supportMapFragment.getMapAsync {
            googleMap = it

            // Turn on the my location layer and the related control on thee map
            updateLocationUI()

            // Get the current location of the device and set the position on thee map
            getDeviceLocation();
        }

        getLocationPermission()
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                applicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        locationPermissionGranted = false
        when(requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true
                }
            }
        }

        updateLocationUI()
    }

    private fun updateLocationUI() {
        googleMap?.let {
            try {
                if (locationPermissionGranted) {
                    it.isMyLocationEnabled = true
                    it.uiSettings.isMyLocationButtonEnabled = true
                } else {
                    it.isMyLocationEnabled = false
                    it.uiSettings.isMyLocationButtonEnabled = false
                    // Set the last know location to null
                    getLocationPermission()
                }
            } catch (exception: SecurityException) {
                Log.e("Error","Unable to find user location", exception)
            }
        }
    }

    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        googleMap?.let {
        if (locationPermissionGranted) {
            val locationResult: Task<*> =
                fusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener(
                this, object : OnCompleteListener<TResult?> {
                    fun onComplete(task: Task<*>) {
                        if (task.isSuccessful) { // Set the map's camera position to the current location of the device.
                            lastKnowLocation = task.result
                            it.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        lastKnowLocation.getLatitude(),
                                        lastKnowLocation.getLongitude()
                                    ), DEFAULT_ZOOM
                                )
                            )
                        } else {
                            Log.d(TAG,"Current location is null. Using defaults.")
                            Log.e(TAG,"Exception: %s",task.exception)
                            it.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation,DEFAULT_ZOOM))
                            it.uiSettings.isMyLocationButtonEnabled = false
                        }
                    }
                })
        } else {

        }
        }
    }
}
