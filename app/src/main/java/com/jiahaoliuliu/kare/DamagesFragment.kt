package com.jiahaoliuliu.kare

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.jiahaoliuliu.kare.databinding.FragmentDamagesBinding

/**
 * A simple [Fragment] subclass.
 * Use the [DamagesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DamagesFragment : Fragment() {

    companion object {
        private const val REQUEST_ID_FRONT_VIEW = 10000
        private const val REQUEST_ID_BACK_VIEW = 10001
        private const val REQUEST_ID_LEFT_VIEW = 10002
        private const val REQUEST_ID_RIGHT_VIEW = 10003
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
        binding.frontView.setOnClickListener{ getCameraPermission(REQUEST_ID_FRONT_VIEW)}
        binding.backView.setOnClickListener{ getCameraPermission(REQUEST_ID_BACK_VIEW)}
        binding.leftView.setOnClickListener{ getCameraPermission(REQUEST_ID_LEFT_VIEW)}
        binding.rightView.setOnClickListener{ getCameraPermission(REQUEST_ID_RIGHT_VIEW)}
        binding.nextButton.isEnabled = false
        binding.comment.addTextChangedListener(object:TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                editable?.let {
                    binding.nextButton.isEnabled = it.isNotEmpty()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }
        })

        binding.nextButton.setOnClickListener{
            Toast.makeText(activity!!, "You are good to go", Toast.LENGTH_LONG).show()
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
                REQUEST_ID_BACK_VIEW -> binding.backView.setImageBitmap(imageBitmap)
                REQUEST_ID_LEFT_VIEW -> binding.leftView.setImageBitmap(imageBitmap)
                REQUEST_ID_RIGHT_VIEW -> binding.rightView.setImageBitmap(imageBitmap)
            }
        }
    }
}