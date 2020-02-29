package com.jiahaoliuliu.kare

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.jiahaoliuliu.kare.databinding.FragmentDamagesBinding

/**
 * A simple [Fragment] subclass.
 * Use the [DamagesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DamagesFragment : Fragment() {

    companion object {
        private const val REQUEST_ID_FRONT_VIEW = 10000
    }

    private lateinit var binding: FragmentDamagesBinding
    private var cameraPermissionGranted = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_damages, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.frontView.setOnClickListener {
            getCameraPermission(REQUEST_ID_FRONT_VIEW)
        }
    }

    private fun getCameraPermission(requestId: Int) {
        if (ContextCompat.checkSelfPermission(
                activity!!.applicationContext, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            cameraPermissionGranted = true
            openCamera(requestId)
        } else {
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), requestId)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray) {
        cameraPermissionGranted = false
        // If request is cancelled, the result arrays are empty
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            cameraPermissionGranted = true
            openCamera(requestCode)
        } else {
            // Camera permission not guaranteed, not do anything
        }
    }

    private fun openCamera(requestId: Int) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                startActivityForResult(takePictureIntent, requestId)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode == Activity.RESULT_OK) {
            val imageBitmap = data.extras?.get("data") as Bitmap
            when(requestCode) {
                REQUEST_ID_FRONT_VIEW -> binding.frontView.setImageBitmap(imageBitmap)
            }
        }
    }
}
