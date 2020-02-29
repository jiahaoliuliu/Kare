package com.jiahaoliuliu.kare

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class LocationFragment: Fragment() {

    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1000
        private val DEFAULT_LOCATION = LatLng(25.276, 55.296)
        private const val DEFAULT_ZOOM = 15F
        private const val TAG = "KARE"
    }

    private var googleMap: GoogleMap? = null
    private var locationPermissionGranted = false
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var lastKnownLocation: Location? = null
    private var onMarkerClickListener: GoogleMap.OnMarkerClickListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity!!)
        val supportMapFragment = childFragmentManager!!.findFragmentById(R.id.maps) as SupportMapFragment
        supportMapFragment.getMapAsync {
            googleMap = it
            getLocationPermission()
        }
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(activity!!.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            onLocationPermissionGuaranteed()
        } else {
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        locationPermissionGranted = false
        when(requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onLocationPermissionGuaranteed()
                } else {
                    onLocationPermissionDenegated()
                }
            }
        }
    }

    private fun onLocationPermissionGuaranteed() {
        locationPermissionGranted = true
        // Turn on the my location layer and the related control on thee map
        updateLocationUI()
        // Get the current location of the device and set the position on thee map
        getDeviceLocation();
    }

    private fun onLocationPermissionDenegated() {
        googleMap?.let {googleMapNotNull ->
            lastKnownLocation?.let { lastKnownLocationNotNull ->
                googleMapNotNull.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(lastKnownLocationNotNull.latitude, lastKnownLocationNotNull.longitude),
                        DEFAULT_ZOOM))
            } ?: run {
                // if the permission is not guaranteed, then use the default location
                googleMapNotNull.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        DEFAULT_LOCATION,
                        DEFAULT_ZOOM
                    )
                )
            }
            googleMapNotNull.uiSettings.isMyLocationButtonEnabled = false
        }
    }

    private fun updateLocationUI() {
        googleMap?.let {
            it.isMyLocationEnabled = true
            it.uiSettings.isMyLocationButtonEnabled = true
        }
    }

    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        googleMap?.let {
            if (!locationPermissionGranted) {
                return
            }
            val locationResult = fusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    lastKnownLocation = task.result
                    val lastKnownLocationLatLng = LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude)
                    it.moveCamera(CameraUpdateFactory.newLatLngZoom(lastKnownLocationLatLng, DEFAULT_ZOOM))

                    val markerOptions = MarkerOptions()
                    markerOptions.position(lastKnownLocationLatLng)
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    it.addMarker(markerOptions)
                    it.setOnMarkerClickListener {marker ->
                        onMarkerClickListener?.onMarkerClick(marker)!!
                    }
                } else {
                    Log.d(TAG, "Current location is null. Using defaults.");
                    Log.e(TAG, "Exception: %s", task.exception);
                    it.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, DEFAULT_ZOOM))
                    it.uiSettings.isMyLocationButtonEnabled = false
                }
            }
        }
    }

    fun setOnMarkerClickListener(onMarkerClickListener: GoogleMap.OnMarkerClickListener) {
        this.onMarkerClickListener = onMarkerClickListener
    }
}